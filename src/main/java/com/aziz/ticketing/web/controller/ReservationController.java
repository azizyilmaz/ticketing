package com.aziz.ticketing.web.controller;

import com.aziz.ticketing.entity.PaymentEntity;
import com.aziz.ticketing.entity.ReservationEntity;
import com.aziz.ticketing.service.ReservationService;
import com.aziz.ticketing.web.dto.ConfirmPaymentRequest;
import com.aziz.ticketing.web.dto.ConfirmPaymentResponse;
import com.aziz.ticketing.web.dto.ReserveSeatRequest;
import com.aziz.ticketing.web.dto.ReserveSeatResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReserveSeatResponse> reserve(@Valid @RequestBody ReserveSeatRequest req) {
        ReservationEntity res = reservationService.reserve(req.getSeatId(), req.getUserId());
        ReserveSeatResponse resp = new ReserveSeatResponse();
        resp.setReservationId(res.getId());
        resp.setSeatId(res.getSeat().getId());
        resp.setStatus(res.getStatus().name());
        resp.setExpiresAt(res.getExpiresAt());
        return ResponseEntity.created(URI.create("/api/reservations/" + res.getId())).body(resp);
    }

    @PostMapping("/confirm")
    public ResponseEntity<ConfirmPaymentResponse> confirm(@Valid @RequestBody ConfirmPaymentRequest req) {
        PaymentEntity payment = reservationService.confirmPayment(req.getReservationId(), req.getUserId(), req.getAmount());
        ConfirmPaymentResponse resp = new ConfirmPaymentResponse();
        resp.setPaymentId(payment.getId());
        resp.setPaymentStatus(payment.getStatus().name());
        resp.setSeatStatus(payment.getReservation().getStatus().name());
        return ResponseEntity.ok(resp);
    }
}