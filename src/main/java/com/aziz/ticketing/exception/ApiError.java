package com.aziz.ticketing.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class ApiError {

    private final OffsetDateTime timestamp = OffsetDateTime.now();

    private int status;

    private String error;

    private String message;

    private String path;
}
