package com.alextomala.vending.exception;

/**
 * A simple exception to tell when someone tries to add an item to an occupied position.
 */
public class PositionOccupiedException extends RuntimeException {
    public PositionOccupiedException(String message) {
        super(message);
    }
}
