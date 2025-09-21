package com.aziz.ticketing.service;

import com.aziz.ticketing.domain.SeatStatus;
import com.aziz.ticketing.entity.SeatEntity;
import com.aziz.ticketing.exception.NotFoundException;
import com.aziz.ticketing.repository.SeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    @Transactional(readOnly = true)
    public SeatEntity getSeatById(Long id) {
        return seatRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Seat not found: " + id));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public SeatEntity lockAndGetSeatById(Long id) {
        return seatRepository.findForUpdate(id)
                .orElseThrow(() -> new NotFoundException("Seat not found: " + id));
    }

    @Transactional
    public void releaseIfExpired(SeatEntity seatEntity, OffsetDateTime now) {
        if (SeatStatus.RESERVED.equals(seatEntity.getStatus())
                && seatEntity.getReservedUntil() != null
                && seatEntity.getReservedUntil().isBefore(now)) {
            seatEntity.setStatus(SeatStatus.AVAILABLE);
            seatEntity.setReservedBy(null);
            seatEntity.setReservedUntil(null);
        }
    }
}
