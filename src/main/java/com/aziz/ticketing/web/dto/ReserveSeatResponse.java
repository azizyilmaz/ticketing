package com.aziz.ticketing.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class ReserveSeatResponse {

    private Long reservationId;

    private Long seatId;

    private String status; // RESERVED or SOLD

    private OffsetDateTime expiresAt;
}
