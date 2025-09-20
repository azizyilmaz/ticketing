package com.aziz.ticketing.repository;

import com.aziz.ticketing.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    List<ReservationEntity> findByExpiresAtBefore(OffsetDateTime ts);
}
