package com.aziz.ticketing.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReserveSeatRequest {

    @NotNull
    private Long seatId;

    @NotNull
    private String userId;
}
