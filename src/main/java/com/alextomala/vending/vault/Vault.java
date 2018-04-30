package com.alextomala.vending.vault;

import com.alextomala.vending.preconditions.Preconditions;
import com.alextomala.vending.exception.InvalidDenominationException;

/**
 * This is a class used to abstract away the currency manipulations from the vending machine.
 *
 * If the VendingMachineHardwareFunctions had more support, I would have made an enum to hold currency types
 * so this could be extended to support different currencies and denominations of currency.
 */
public class Vault {
    private int nickels;
    private int dimes;
    private int quarters;

    public Vault() {
        nickels = 0;
        dimes = 0;
        quarters = 0;
    }

    public void addMoney(Integer cents) {
        switch (Preconditions.checkNotNull(cents)) {
            case 5:
                nickels++;
                break;
            case 10:
                dimes++;
                break;
            case 25:
                quarters++;
                break;
            default:
                throw new InvalidDenominationException(String.format("User entered invalid amount of money %d", cents));
        }
    }

    public void clearVault() {
        nickels = 0;
        dimes = 0;
        quarters = 0;
    }

    public int getTotalMoney() {
        return nickels * 5 + dimes * 10 + quarters * 25;
    }

    public void addNickel() {
        nickels++;
    }

    public void addDime() {
        dimes++;
    }

    public void addQuarter() {
        quarters++;
    }

    public int getNickels() {
        return nickels;
    }

    public int getDimes() {
        return dimes;
    }

    public int getQuarters() {
        return quarters;
    }

    /**
     * The removeNickels function will NEVER remove more than the number of nickels in the vault.
     * It is up to the caller to see how many nickels were actually removed.
     */
    public int removeNickels(int nickelsToRemove) {
        int nickelsRemoved = Math.min(Preconditions.checkPositive(nickelsToRemove), nickels);
        nickels -= nickelsRemoved;
        return nickelsRemoved;
    }

    /**
     * The removeDimes function will NEVER remove more than the number of dimes in the vault.
     * It is up to the caller to see how many dimes were actually removed.
     */
    public int removeDimes(int dimesToRemove) {
        int dimesRemoved = Math.min(Preconditions.checkPositive(dimesToRemove), dimes);
        dimes -= dimesRemoved;
        return dimesRemoved;
    }

    /**
     * The removeQuarters function will NEVER remove more than the number of quarters in the vault.
     * It is up to the caller to see how many quarters were actually removed.
     */
    public int removeQuarters(int quartersToRemove) {
        int quartersRemoved = Math.min(Preconditions.checkPositive(quartersToRemove), quarters);
        quarters -= quartersRemoved;
        return quartersRemoved;
    }
}
