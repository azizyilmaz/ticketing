package com.aziz.ticketing.scheduler;

import com.aziz.ticketing.domain.SeatStatus;
import com.aziz.ticketing.entity.ReservationEntity;
import com.aziz.ticketing.entity.SeatEntity;
import com.aziz.ticketing.repository.ReservationRepository;
import com.aziz.ticketing.service.SeatService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Component
public class ReservationCleanupJob {

    private final ReservationRepository reservationRepository;
    private final SeatService seatService;

    public ReservationCleanupJob(ReservationRepository reservationRepository, SeatService seatService) {
        this.reservationRepository = reservationRepository;
        this.seatService = seatService;
    }

    @Scheduled(fixedRate = 30000) // every 30s
    @Transactional
    public void releaseExpired() {
        OffsetDateTime now = OffsetDateTime.now();
        List<ReservationEntity> expired = reservationRepository.findByExpiresAtBefore(now);
        for (ReservationEntity reservation : expired) {
            SeatEntity seatEntity = seatService.lockAndGetSeatById(reservation.getSeat().getId());
            if (SeatStatus.RESERVED.equals(seatEntity.getStatus())
                    && (seatEntity.getReservedUntil() == null || seatEntity.getReservedUntil().isBefore(now))) {
                seatEntity.setStatus(SeatStatus.AVAILABLE);
                seatEntity.setReservedBy(null);
                seatEntity.setReservedUntil(null);
            }
        }
    }
}
