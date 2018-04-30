package com.alextomala.vending;

import java.util.Map;

/**
 * A Generic Vending Machine contract
 */
public interface VendingMachine {

    /**
     * User Function - This is called when a user presses a button for a particular product. This is used for both price
     * checking and purchasing.
     */
    void buttonPress(Integer productPosition);

    /**
     * User Function - This is called when the user adds money to the machine. The cents parameter represent the value
     * of the particular currency added to the machine. For example, when the user adds a Nickel, this function will be
     * called with a value of 5.
     *
     * Note: Only one coin will be added at a time. Only Nickels, Dimes, and Quarters will be added.
     */
    void addUserMoney(Integer cents);

    /**
     * User Function - This is called when a user has decided to cancel the order.  All change inserted will be
     * returned to the user.
     */
    void cancelOrder();


    /**
     * The following are the admin functions.  Personally, I think these should be in a different interface to split
     * up the public functions from the ones an admin can do.
     */

    /**
     * Admin Function - Adds more product to the vending machine.
     *
     * Product details will contain:
     *   Key: "price", Value: an integer represent the product price in cents.
     */
    void addProduct(Integer productPosition, Map<String, Object> productDetails);

    /**
     * Admin Function - Adds coins to the machine to be used when making change.
     */
    void addChange(Integer numberOfNickels, Integer numberOfDimes, Integer numberOfQuarters);

    /**
     * Admin Function - Dispense all change in the vending machine.
     */
    void dispenseAllChange();
}