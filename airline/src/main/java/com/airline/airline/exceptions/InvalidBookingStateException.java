package com.airline.airline.exceptions;

public class InvalidBookingStateException extends Exception {
    public InvalidBookingStateException(String message) {
        super(message);
    }
}
