package com.alextomala.vending.vault;

import com.alextomala.vending.exception.InvalidDenominationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VaultTest {

    private Vault vault;

    @Before
    public void setUp() throws Exception {
        vault = new Vault();
    }

    @After
    public void tearDown() throws Exception {
        vault = null;
    }

    @Test
    public void testAddMoney_validCase_nickel() {
        vault.addMoney(5);
        assertEquals(5, vault.getTotalMoney());
    }

    @Test
    public void testAddMoney_validCase_dime() {
        vault.addMoney(10);
        assertEquals(10, vault.getTotalMoney());
    }

    @Test
    public void testAddMoney_validCase_quarter() {
        vault.addMoney(25);
        assertEquals(25, vault.getTotalMoney());
    }

    @Test(expected = InvalidDenominationException.class)
    public void testAddMoney_validCase_penny() {
        vault.addMoney(1);
    }

    @Test(expected = InvalidDenominationException.class)
    public void testAddMoney_validCase_negative() {
        vault.addMoney(-1);
    }

    @Test(expected = NullPointerException.class)
    public void testAddMoney_validCase_null() {
        vault.addMoney(null);
    }

    @Test
    public void testClearVault() {
        vault.addMoney(25);
        assertEquals(25, vault.getTotalMoney());

        vault.clearVault();
        assertEquals(0, vault.getTotalMoney());
    }

    @Test
    public void testAddNickel() {
        vault.addNickel();
        assertEquals(5, vault.getTotalMoney());
        assertEquals(1, vault.getNickels());
    }

    @Test
    public void testAddDime() {
        vault.addDime();
        assertEquals(10, vault.getTotalMoney());
        assertEquals(1, vault.getDimes());
    }

    @Test
    public void testAddQuarter() {
        vault.addQuarter();
        assertEquals(25, vault.getTotalMoney());
        assertEquals(1, vault.getQuarters());
    }

    @Test
    public void testRemoveNickels_noNickelsToRemove() {
        int nickelsRemoved = vault.removeNickels(5);
        assertEquals(0, nickelsRemoved);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNickels_negativeNumberPassedIn() {
        vault.removeNickels(-1);
    }

    @Test
    public void testRemoveNickels_removeFewerThanInVault() {
        vault.addNickel();
        vault.addNickel();
        vault.addNickel();

        int nickelsRemoved = vault.removeNickels(2);
        assertEquals(2, nickelsRemoved);
        assertEquals(1, vault.getNickels());

        nickelsRemoved = vault.removeNickels(2);
        assertEquals(1, nickelsRemoved);
        assertEquals(0, vault.getNickels());

        nickelsRemoved = vault.removeNickels(100);
        assertEquals(0, nickelsRemoved);
        assertEquals(0, vault.getNickels());
    }

    @Test
    public void testRemoveDimes_fullTest() {
        vault.addDime();
        vault.addDime();
        vault.addDime();

        int dimesRemoved = vault.removeDimes(2);
        assertEquals(2, dimesRemoved);
        assertEquals(1, vault.getDimes());

        dimesRemoved = vault.removeDimes(2);
        assertEquals(1, dimesRemoved);
        assertEquals(0, vault.getDimes());

        dimesRemoved = vault.removeDimes(100);
        assertEquals(0, dimesRemoved);
        assertEquals(0, vault.getDimes());
    }

    @Test
    public void testRemoveQuarters_fullTest() {
        vault.addQuarter();
        vault.addQuarter();
        vault.addQuarter();

        int quartersRemoved = vault.removeQuarters(2);
        assertEquals(2, quartersRemoved);
        assertEquals(1, vault.getQuarters());

        quartersRemoved = vault.removeQuarters(2);
        assertEquals(1, quartersRemoved);
        assertEquals(0, vault.getQuarters());

        quartersRemoved = vault.removeQuarters(100);
        assertEquals(0, quartersRemoved);
        assertEquals(0, vault.getQuarters());
    }
}