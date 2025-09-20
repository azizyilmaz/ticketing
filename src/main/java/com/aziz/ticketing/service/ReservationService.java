package com.aziz.ticketing.service;

import com.aziz.ticketing.domain.PaymentStatus;
import com.aziz.ticketing.domain.SeatStatus;
import com.aziz.ticketing.entity.PaymentEntity;
import com.aziz.ticketing.entity.ReservationEntity;
import com.aziz.ticketing.entity.SeatEntity;
import com.aziz.ticketing.exception.SeatAlreadyReservedException;
import com.aziz.ticketing.exception.SeatAlreadySoldException;
import com.aziz.ticketing.repository.PaymentRepository;
import com.aziz.ticketing.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
public class ReservationService {

    private final SeatService seatService;
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;

    @Value("${app.reservation-hold-minutes:10}")
    private int holdMinutes;

    public ReservationService(SeatService seatService, ReservationRepository reservationRepository, PaymentRepository paymentRepository) {
        this.seatService = seatService;
        this.reservationRepository = reservationRepository;
        this.paymentRepository = paymentRepository;
    }

    /**
     * Reserve a seat with pessimistic row lock. Short transaction.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public ReservationEntity reserve(Long seatId, String userId) {
        OffsetDateTime now = OffsetDateTime.now();

        // 1) Lock the seat row
        SeatEntity seatEntity = seatService.lockAndGetSeatById(seatId);

        // 2) Release if any stale reservation
        seatService.releaseIfExpired(seatEntity, now);

        // 3) Validate status
        if (SeatStatus.SOLD.equals(seatEntity.getStatus())) {
            throw new SeatAlreadySoldException("Seat already sold: " + seatId);
        }
        if (SeatStatus.RESERVED.equals(seatEntity.getStatus())
                && seatEntity.getReservedUntil() != null
                && seatEntity.getReservedUntil().isAfter(now)) {
            throw new SeatAlreadyReservedException("Seat already reserved until " + seatEntity.getReservedUntil());
        }

        // 4) Reserve
        seatEntity.setStatus(SeatStatus.RESERVED);
        seatEntity.setReservedBy(userId);
        seatEntity.setReservedUntil(now.plusMinutes(holdMinutes));

        ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setSeat(seatEntity);
        reservationEntity.setUserId(userId);
        reservationEntity.setReservedAt(now);
        reservationEntity.setExpiresAt(seatEntity.getReservedUntil());
        reservationEntity.setStatus(SeatStatus.RESERVED);

        return reservationRepository.save(reservationEntity);
    }

    /**
     * Confirm payment: capture then mark seat SOLD atomically
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public PaymentEntity confirmPayment(Long reservationId, String userId, BigDecimal amount) {
        ReservationEntity res = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found:" + reservationId));

        // Lock the seat to avoid race during sell
        SeatEntity seat = seatService.lockAndGetSeatById(res.getSeat().getId());
        OffsetDateTime now = OffsetDateTime.now();
        if (res.getExpiresAt().isBefore(now)) {
            // Expired -> release
            seatService.releaseIfExpired(seat, now);
            throw new SeatAlreadyReservedException("Reservation expired");
        }
        if (!userId.equals(res.getUserId())) {
            throw new IllegalArgumentException("Reservation belongs to another user");
        }
        if (SeatStatus.SOLD.equals(seat.getStatus())) {
            throw new SeatAlreadySoldException("Seat already sold");
        }
        // Payment flow (simplified: capture)
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setReservation(res);
        paymentEntity.setUserId(userId);
        paymentEntity.setAmount(amount);
        paymentEntity.setStatus(PaymentStatus.CAPTURED);
        paymentEntity.setCreatedAt(now);
        paymentRepository.save(paymentEntity);

        // Mark seat SOLD and reservation SOLD
        seat.setStatus(SeatStatus.SOLD);
        seat.setReservedUntil(null);
        res.setStatus(SeatStatus.SOLD);
        return paymentEntity;
    }
}