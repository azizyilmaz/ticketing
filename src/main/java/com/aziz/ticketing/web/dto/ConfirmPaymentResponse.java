package com.aziz.ticketing.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmPaymentResponse {

    private Long paymentId;
    
    private String paymentStatus; // AUTHORIZED/CAPTURED/FAILED

    private String seatStatus; // RESERVED/SOLD
}
