package com.alextomala.vending.preconditions;

/**
 * Since I can't import guava, I'll just write it myself.
 */
public class Preconditions {

    /**
     * Ensure a reference is not null.
     */
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    /**
     * Ensure a positive integer.  We cannot have negative money or insert items into negative positions.
     */
    public static Integer checkPositive(Integer reference) {
        if (Preconditions.checkNotNull(reference) < 0) {
            throw new IllegalArgumentException("A number cannot be negative");
        }
        return reference;
    }
}
