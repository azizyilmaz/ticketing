package com.aziz.ticketing.exception;

public class SeatAlreadySoldException extends RuntimeException {

    public SeatAlreadySoldException(String message) {
        super(message);
    }
}
