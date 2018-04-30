package com.alextomala.vending.exception;

/**
 * An exception to tell the user that there isn't enough change to distribute.
 *
 * Sorry user!
 */
public class NotEnoughChangeException extends RuntimeException {

    public NotEnoughChangeException(String message) {
        super(message);
    }
}
