package com.aziz.ticketing.entity;

import com.aziz.ticketing.domain.SeatStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "reservations", indexes = {
        @Index(name = "ix_res_by_user", columnList = "userId"),
        @Index(name = "ix_res_by_seat", columnList = "seat_id")})
@Getter
@Setter
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private SeatEntity seat;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private OffsetDateTime reservedAt;

    @Column(nullable = false)
    private OffsetDateTime expiresAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus status; // RESERVED or SOLD
}