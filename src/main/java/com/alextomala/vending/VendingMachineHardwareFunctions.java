package com.alextomala.vending;

/**
 * This object is intended to abstract the interaction with the vending machine's user interface.
 * <p>
 * Note: These functions do not validate if they function correctly, meaning you can call `dispenseNickel` even if the
 * machine does not contain any nickels and it will not return any error, however you should not call `dispenseNickel`
 * if the machine does not contain any nickels.
 */
public class VendingMachineHardwareFunctions {

    public static void showMessage(String message) {
        System.out.println(message);
    }

    public static void dispenseProduct(Integer productPosition, String productName) {
        String nullSafeProductName = (productName != null) ? productName : "ProductNum" + productPosition;
        System.out.println("Dispensing " + nullSafeProductName + " from position " + productPosition);
    }

    public static void dispenseNickel() {
        System.out.println("Dispensing 5 cents");
    }

    public static void dispenseDime() {
        System.out.println("Dispensing 10 cents");
    }

    public static void dispenseQuarter() {
        System.out.println("Dispensing 25 cents");
    }
}
