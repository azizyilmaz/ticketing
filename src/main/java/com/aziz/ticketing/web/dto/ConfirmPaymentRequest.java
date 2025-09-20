package com.aziz.ticketing.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ConfirmPaymentRequest {

    @NotNull
    private Long reservationId;

    @NotBlank
    private String userId;

    @NotNull
    @Positive
    private BigDecimal amount;
}
