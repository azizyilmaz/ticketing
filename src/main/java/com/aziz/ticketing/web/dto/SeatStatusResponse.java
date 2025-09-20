package com.aziz.ticketing.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class SeatStatusResponse {

    private Long seatId;
    
    private String status;

    private String reservedBy;

    private OffsetDateTime reservedUntil;
}
