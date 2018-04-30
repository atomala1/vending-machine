package com.alextomala.vending.impl;

import com.alextomala.vending.VendingMachineHardwareFunctions;
import com.alextomala.vending.exception.InvalidDenominationException;
import com.alextomala.vending.exception.PositionOccupiedException;
import com.alextomala.vending.vault.Vault;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@PrepareForTest({VendingMachineHardwareFunctions.class})
@RunWith(PowerMockRunner.class)
public class JavaVendingMachineTest {

    private JavaVendingMachine vendingMachine;

    @Before
    public void setUp() {
        mockStatic(VendingMachineHardwareFunctions.class);
        vendingMachine = new JavaVendingMachine();
    }

    @After
    public void tearDown() {
        vendingMachine = null;
    }

    @Test
    public void testAddUserMoney_addNickel() throws Exception {
        vendingMachine.addUserMoney(5);

        Field f = vendingMachine.getClass().getDeclaredField("userVault");
        f.setAccessible(true);
        Vault userVault = (Vault) f.get(vendingMachine);
        assertEquals(5, userVault.getTotalMoney());
    }

    @Test
    public void testAddUserMoney_addDime() throws Exception {
        vendingMachine.addUserMoney(10);

        Field f = vendingMachine.getClass().getDeclaredField("userVault");
        f.setAccessible(true);
        Vault userVault = (Vault) f.get(vendingMachine);
        assertEquals(10, userVault.getTotalMoney());
    }

    @Test
    public void testAddUserMoney_addQuarter() throws Exception {
        vendingMachine.addUserMoney(25);

        Field f = vendingMachine.getClass().getDeclaredField("userVault");
        f.setAccessible(true);
        Vault userVault = (Vault) f.get(vendingMachine);
        assertEquals(25, userVault.getTotalMoney());
    }

    @Test
    public void testAddUserMoney_oneOfEach() throws Exception {
        vendingMachine.addUserMoney(5);
        vendingMachine.addUserMoney(10);
        vendingMachine.addUserMoney(25);

        Field f = vendingMachine.getClass().getDeclaredField("userVault");
        f.setAccessible(true);
        Vault userVault = (Vault) f.get(vendingMachine);
        assertEquals(40, userVault.getTotalMoney());
    }

    @Test(expected = InvalidDenominationException.class)
    public void testAddUserMoney_invalidDenomination() {
        vendingMachine.addUserMoney(9);
    }

    @Test(expected = NullPointerException.class)
    public void testAddUserMoney_null() {
        vendingMachine.addUserMoney(null);
    }

    @Test
    public void testButtonPress_noItems() {
        vendingMachine.buttonPress(3);

        verifyStatic(VendingMachineHardwareFunctions.class);
        VendingMachineHardwareFunctions.showMessage("No item in position 3");

        verifyNoMoreInteractions(VendingMachineHardwareFunctions.class);
    }

    @Test(expected = RuntimeException.class)
    public void testAddChange_negativeChange() {
        vendingMachine.addChange(-1, 0, 0);
    }

    @Test
         public void testCancelOrder() {
        vendingMachine.addChange(1, 0, 5);
        vendingMachine.addUserMoney(5);

        vendingMachine.cancelOrder();
        verifyStatic(VendingMachineHardwareFunctions.class, times(1));
        VendingMachineHardwareFunctions.dispenseNickel();

        verifyNoMoreInteractions(VendingMachineHardwareFunctions.class);
    }

    @Test
    public void testDispenseAllChange() {
        vendingMachine.addChange(1, 0, 5);
        vendingMachine.addUserMoney(5);

        vendingMachine.dispenseAllChange();
        verifyStatic(VendingMachineHardwareFunctions.class, times(1));
        VendingMachineHardwareFunctions.dispenseNickel();
        verifyStatic(VendingMachineHardwareFunctions.class, times(5));
        VendingMachineHardwareFunctions.dispenseQuarter();

        verifyNoMoreInteractions(VendingMachineHardwareFunctions.class);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAddProduct_validCase() throws Exception {
        // I would like ImmutableMap.of() here.
        Map<String, Object> productDescription = new HashMap<>();
        productDescription.put("price", 5);
        productDescription.put("name", "Peanuts");

        vendingMachine.addProduct(1, productDescription);

        Field f = vendingMachine.getClass().getDeclaredField("items");
        f.setAccessible(true);
        Map<Integer, Map<String, Object>> items = (Map<Integer, Map<String, Object>>) f.get(vendingMachine);
        assertEquals(1, items.size());
    }

    @Test(expected = PositionOccupiedException.class)
    public void testAddProduct_positionOccupied() throws Exception {
        // I would like ImmutableMap.of() here.
        Map<String, Object> productDescription = new HashMap<>();
        productDescription.put("price", 5);
        productDescription.put("name", "Peanuts");

        vendingMachine.addProduct(1, productDescription);
        vendingMachine.addProduct(1, productDescription);
    }

    @Test(expected = InvalidDenominationException.class)
    public void testAddProduct_invalidDenomination() throws Exception {
        // I would like ImmutableMap.of() here.
        Map<String, Object> productDescription = new HashMap<>();
        productDescription.put("price", 6);
        productDescription.put("name", "Peanuts");

        vendingMachine.addProduct(1, productDescription);
    }

    @Test(expected = InvalidDenominationException.class)
    public void testAddProduct_priceIsNull() throws Exception {
        // I would like ImmutableMap.of() here.
        Map<String, Object> productDescription = new HashMap<>();
        productDescription.put("price", null);

        vendingMachine.addProduct(1, productDescription);
    }

    @Test(expected = InvalidDenominationException.class)
    public void testAddProduct_negativePrice() throws Exception {
        // I would like ImmutableMap.of() here.
        Map<String, Object> productDescription = new HashMap<>();
        productDescription.put("price", -1);

        vendingMachine.addProduct(1, productDescription);
    }

    @Test(expected = InvalidDenominationException.class)
    public void testAddProduct_nameIsNotString() throws Exception {
        // I would like ImmutableMap.of() here.
        Map<String, Object> productDescription = new HashMap<>();
        productDescription.put("price", "1");
        productDescription.put("name", 1);

        vendingMachine.addProduct(1, productDescription);
    }

    @Test
    public void testAddProduct_nameIsEmptyString() throws Exception {
        // I still would like ImmutableMap.of() here.
        Map<String, Object> productDescription = new HashMap<>();
        productDescription.put("price", 50);
        productDescription.put("name", "");

        vendingMachine.addProduct(1, productDescription);

        verifyStatic(VendingMachineHardwareFunctions.class, times(1));
        VendingMachineHardwareFunctions.showMessage("Product name is missing for product {price=50, name=}");

        verifyNoMoreInteractions(VendingMachineHardwareFunctions.class);
    }

    @Test
    public void test_everything() {
        vendingMachine.buttonPress(3);

        Map<String, Object> peanutDescription = new HashMap<>();
        peanutDescription.put("price", 5);
        vendingMachine.addProduct(1, peanutDescription);

        Map<String, Object> candyBarDescription = new HashMap<>();
        candyBarDescription.put("price", 15);
        candyBarDescription.put("name", "Candy Bar");
        vendingMachine.addProduct(2, candyBarDescription);

        Map<String, Object> gumDescription = new HashMap<>();
        gumDescription.put("price", 50);
        gumDescription.put("name", "Gum");
        vendingMachine.addProduct(3, gumDescription);

        vendingMachine.buttonPress(3);

        vendingMachine.addUserMoney(25);
        vendingMachine.addUserMoney(25);
        vendingMachine.addUserMoney(5);

        vendingMachine.buttonPress(3);
        vendingMachine.addUserMoney(5);
        vendingMachine.buttonPress(1);

        verifyStatic(VendingMachineHardwareFunctions.class, times(1));
        VendingMachineHardwareFunctions.showMessage("No item in position 3");
        verifyStatic(VendingMachineHardwareFunctions.class, times(1));
        VendingMachineHardwareFunctions.showMessage("Product name is missing for product {price=5}");
        verifyStatic(VendingMachineHardwareFunctions.class, times(1));
        VendingMachineHardwareFunctions.showMessage("Item in position 3 costs 50");
        verifyStatic(VendingMachineHardwareFunctions.class, times(1));
        VendingMachineHardwareFunctions.dispenseProduct(3, "Gum");
        verifyStatic(VendingMachineHardwareFunctions.class, times(1));
        VendingMachineHardwareFunctions.dispenseNickel();
        verifyStatic(VendingMachineHardwareFunctions.class, times(1));
        VendingMachineHardwareFunctions.dispenseProduct(1, null);

        verifyNoMoreInteractions(VendingMachineHardwareFunctions.class);
    }

    @Test(expected = RuntimeException.class)
    public void test_noChangeToDispense() {
        Map<String, Object> peanutDescription = new HashMap<>();
        peanutDescription.put("price", 40);
        peanutDescription.put("name", "Candy Bar");
        vendingMachine.addProduct(1, peanutDescription);

        vendingMachine.addUserMoney(25);
        vendingMachine.addUserMoney(25);

        vendingMachine.buttonPress(1);
    }

    @Test
    public void test_adminAddedChangeFirst() {
        vendingMachine.addChange(0, 1, 0);

        Map<String, Object> peanutDescription = new HashMap<>();
        peanutDescription.put("price", 40);
        peanutDescription.put("name", "Candy Bar");
        vendingMachine.addProduct(1, peanutDescription);

        vendingMachine.addUserMoney(25);
        vendingMachine.addUserMoney(25);

        vendingMachine.buttonPress(1);

        verifyStatic(VendingMachineHardwareFunctions.class, times(1));
        VendingMachineHardwareFunctions.dispenseProduct(1, "Candy Bar");
        verifyStatic(VendingMachineHardwareFunctions.class, times(1));
        VendingMachineHardwareFunctions.dispenseDime();

        verifyNoMoreInteractions(VendingMachineHardwareFunctions.class);
    }

    @Test(expected = NullPointerException.class)
    public void testButtonPress_nullPressed() {
        vendingMachine.buttonPress(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testButtonPress_negativeNumberPressed() {
        vendingMachine.buttonPress(-1);
    }

    @Test(expected = NullPointerException.class)
    public void testAddChange_onlyNullSupplied() {
        vendingMachine.addChange(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddProduct_negativeProductPosition() {
        vendingMachine.addProduct(-1, Collections.emptyMap());
    }

    @Test
    public void test_buyProductThatCostsDime() {
        Map<String, Object> gumDescription = new HashMap<>();
        gumDescription.put("price", 10);
        gumDescription.put("name", "Gum");
        vendingMachine.addProduct(1, gumDescription);

        vendingMachine.addUserMoney(10);

        vendingMachine.buttonPress(1);

        verifyStatic(VendingMachineHardwareFunctions.class, times(1));
        VendingMachineHardwareFunctions.dispenseProduct(1, "Gum");

        verifyNoMoreInteractions(VendingMachineHardwareFunctions.class);
    }

    @Test
    public void test_buyProductThatCostsDime_dispenseNickelAndDime() {
        vendingMachine.addChange(1, 1, 0);

        Map<String, Object> gumDescription = new HashMap<>();
        gumDescription.put("price", 10);
        gumDescription.put("name", "Gum");
        vendingMachine.addProduct(1, gumDescription);

        vendingMachine.addUserMoney(25);

        vendingMachine.buttonPress(1);

        verifyStatic(VendingMachineHardwareFunctions.class, times(1));
        VendingMachineHardwareFunctions.dispenseProduct(1, "Gum");
        verifyStatic(VendingMachineHardwareFunctions.class, times(1));
        VendingMachineHardwareFunctions.dispenseDime();
        verifyStatic(VendingMachineHardwareFunctions.class, times(1));
        VendingMachineHardwareFunctions.dispenseNickel();

        verifyNoMoreInteractions(VendingMachineHardwareFunctions.class);
    }
}