package com.alextomala.vending.exception;

/**
 * A simple class to give a name to the invalid denomination exception.
 */
public class InvalidDenominationException extends RuntimeException {

    public InvalidDenominationException(String message) {
        super(message);
    }
}
