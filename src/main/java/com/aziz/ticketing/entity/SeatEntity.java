package com.aziz.ticketing.entity;

import com.aziz.ticketing.domain.SeatStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "seats", indexes = {
        @Index(name = "ix_seat_event", columnList = "event_id"),
        @Index(name = "ix_seat_status", columnList = "status")})
@Getter
@Setter
public class SeatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    @Column(nullable = false)
    private String seatNumber; // e.g., S-1

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus status = SeatStatus.AVAILABLE;

    private String reservedBy; // user id

    private OffsetDateTime reservedUntil;

    @Version
    private Long version; // extra safety (optimistic)
}