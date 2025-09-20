package com.aziz.ticketing.web.controller;

import com.aziz.ticketing.entity.SeatEntity;
import com.aziz.ticketing.service.SeatService;
import com.aziz.ticketing.web.dto.SeatStatusResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/seats")
public class SeatController {
    
    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping("/{id}")
    public SeatStatusResponse get(@PathVariable Long id) {
        SeatEntity s = seatService.getSeatById(id);
        SeatStatusResponse resp = new SeatStatusResponse();
        resp.setSeatId(s.getId());
        resp.setStatus(s.getStatus().name());
        resp.setReservedBy(s.getReservedBy());
        resp.setReservedUntil(s.getReservedUntil());
        return resp;
    }
}