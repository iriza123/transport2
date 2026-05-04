package com.transport.backend.exceptions;

public class CapacityExceededException extends Exception {
    public CapacityExceededException(String message) {
        super(message);
    }
}

