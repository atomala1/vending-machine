package com.alextomala.vending.impl;

import com.alextomala.vending.preconditions.Preconditions;
import com.alextomala.vending.vault.Vault;
import com.alextomala.vending.VendingMachine;
import com.alextomala.vending.VendingMachineHardwareFunctions;
import com.alextomala.vending.exception.InvalidDenominationException;
import com.alextomala.vending.exception.NotEnoughChangeException;
import com.alextomala.vending.exception.PositionOccupiedException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

public class JavaVendingMachine implements VendingMachine {
    private final Vault userVault;
    private final Vault systemVault;

    Map<Integer, Map<String, Object>> items;

    public JavaVendingMachine() {
        userVault = new Vault();
        systemVault = new Vault();

        items = new HashMap<>();
    }

    @Override
    public void buttonPress(Integer productPosition) {
        Map<String, Object> item = items.get(Preconditions.checkPositive(productPosition));
        if (item != null) {
            int itemPrice = (Integer) item.get("price");
            int userMoney = userVault.getTotalMoney();
            if (userMoney >= itemPrice) {
                VendingMachineHardwareFunctions.dispenseProduct(productPosition, (String) item.get("name"));
                addUserMoneyToSystemMoney();
                dispenseChangeAfterSale(userMoney - itemPrice);
            } else {
                String message = String.format("Item in position %d costs %d", productPosition, (Integer) item.get("price"));
                VendingMachineHardwareFunctions.showMessage(message);
            }
        } else {
            String message = String.format("No item in position %d", productPosition);
            VendingMachineHardwareFunctions.showMessage(message);
        }
    }

    @Override
    public void addUserMoney(Integer cents) {
        userVault.addMoney(cents);
    }

    @Override
    public void cancelOrder() {
        dispenseChange(userVault.getNickels(), userVault.getDimes(), userVault.getQuarters());
        userVault.clearVault();
    }

    @Override
    public void dispenseAllChange() {
        dispenseChange(systemVault.getNickels(), systemVault.getDimes(), systemVault.getQuarters());
        systemVault.clearVault();
    }

    private void addUserMoneyToSystemMoney() {
        addChange(userVault.getNickels(), userVault.getDimes(), userVault.getQuarters());
        userVault.clearVault();
    }

    private void dispenseChangeAfterSale(Integer amountToDispense) {
        Preconditions.checkPositive(amountToDispense);

        int quartersToDispense = Math.round(amountToDispense / 25);
        int quartersRemoved = systemVault.removeQuarters(quartersToDispense);
        amountToDispense -= quartersRemoved * 25;

        int dimesToDispense = Math.round(amountToDispense / 10);
        int dimesRemoved = systemVault.removeDimes(dimesToDispense);
        amountToDispense -= dimesRemoved * 10;

        int nickelsToDispense = Math.round(amountToDispense / 5);
        int nickelsRemoved = systemVault.removeNickels(nickelsToDispense);
        amountToDispense -= nickelsRemoved * 5;

        // I wasn't sure what to do in this case, so I just threw an exception
        if (amountToDispense != 0) {
            throw new NotEnoughChangeException("There was not enough change to dispense correctly.");
        }

        dispenseChange(nickelsRemoved, dimesRemoved, quartersRemoved);
    }

    @Override
    public void addProduct(Integer productPosition, Map<String, Object> productDetails) {
        checkProductPosition(productPosition);
        checkProductPrice(productDetails);
        checkProductName(productDetails);

        items.put(productPosition, productDetails);
    }

    private void checkProductPosition(Integer productPosition) {
        Map<String, Object> item = items.get(Preconditions.checkPositive(productPosition));
        if (item != null) {
            throw new PositionOccupiedException(String.format("Product position %d is already occupied", productPosition));
        }
    }

    /**
     * The product price is checked against %5 because the smallest denomination we know of is a nickel.
     *
     * If this was going to be extended, I would add a getSmallestDenomination() function to the Denomination Enum.
     */
    private Integer checkProductPrice(Map<String, Object> productDetails) {
        Object price = productDetails.get("price");
        if (price != null && price instanceof Integer && (Integer) price > 0 && (Integer) price % 5 == 0) {
            return (Integer) price;
        } else {
            throw new InvalidDenominationException("Price was not in a valid format");
        }
    }

    private Optional<String> checkProductName(Map<String, Object> productDetails) {
        Object name = productDetails.get("name");
        if (name != null && name instanceof String && ((String) name).length() > 0) {
            return Optional.of((String) name);
        } else {
            String message = String.format("Product name is missing for product %s", productDetails);
            VendingMachineHardwareFunctions.showMessage(message);
            return Optional.empty();
        }
    }

    @Override
    public void addChange(Integer numberOfNickels, Integer numberOfDimes, Integer numberOfQuarters) {
        IntStream.rangeClosed(1, Preconditions.checkPositive(numberOfNickels)).forEach(nickel -> systemVault.addNickel());
        IntStream.rangeClosed(1, Preconditions.checkPositive(numberOfDimes)).forEach(nickel -> systemVault.addDime());
        IntStream.rangeClosed(1, Preconditions.checkPositive(numberOfQuarters)).forEach(nickel -> systemVault.addQuarter());
    }

    private void dispenseChange(Integer nickels, Integer dimes, Integer quarters) {
        IntStream.rangeClosed(1, nickels).forEach(nickel -> VendingMachineHardwareFunctions.dispenseNickel());
        IntStream.rangeClosed(1, dimes).forEach(dime -> VendingMachineHardwareFunctions.dispenseDime());
        IntStream.rangeClosed(1, quarters).forEach(dime -> VendingMachineHardwareFunctions.dispenseQuarter());
    }
}
