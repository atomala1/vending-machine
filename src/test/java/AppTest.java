import com.alextomala.vending.VendingMachine;
import com.alextomala.vending.VendingMachineHardwareFunctions;
import com.alextomala.vending.exception.PositionOccupiedException;
import com.alextomala.vending.impl.JavaVendingMachine;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * These are the workflow tests from the email.
 */
@PrepareForTest({VendingMachineHardwareFunctions.class})
@RunWith(PowerMockRunner.class)
public class AppTest {

    @Test
    public void testAppHasAGreeting() {
        App classUnderTest = new App();
        assertNotNull("app should have a greeting", classUnderTest.getGreeting());
    }

    private VendingMachine vendingMachine;

    @Before
    public void setUp() {
        mockStatic(VendingMachineHardwareFunctions.class);
        vendingMachine = new JavaVendingMachine();

        Map<String, Object> peanutDescription = new HashMap<>();
        peanutDescription.put("price", 50);
        peanutDescription.put("name", "Peanuts");
        vendingMachine.addProduct(1, peanutDescription);

        Map<String, Object> candyBarDescription = new HashMap<>();
        candyBarDescription.put("price", 15);
        candyBarDescription.put("name", "Candy Bar");
        vendingMachine.addProduct(2, candyBarDescription);

        Map<String, Object> gumDescription = new HashMap<>();
        gumDescription.put("price", 50);
        gumDescription.put("name", "Gum");
        vendingMachine.addProduct(3, gumDescription);
    }

    @After
    public void tearDown() {
        vendingMachine = null;
    }

    @Test
    public void test_workflow1() {
        vendingMachine.buttonPress(3);

        verifyStatic(VendingMachineHardwareFunctions.class, times(1));
        VendingMachineHardwareFunctions.showMessage("Item in position 3 costs 50");

        verifyNoMoreInteractions(VendingMachineHardwareFunctions.class);
    }

    @Test
    public void test_workflow2() {
        vendingMachine.addUserMoney(25);
        vendingMachine.addUserMoney(25);

        vendingMachine.buttonPress(1);

        verifyStatic(VendingMachineHardwareFunctions.class, times(1));
        VendingMachineHardwareFunctions.dispenseProduct(1, "Peanuts");

        verifyNoMoreInteractions(VendingMachineHardwareFunctions.class);
    }

    @Test(expected = PositionOccupiedException.class)
    public void test_workflow3_filledPosition() {
        vendingMachine.addProduct(1, Collections.emptyMap());
    }

    @Test
    public void test_workflow3() {
        Map<String, Object> granolaBarDescription = new HashMap<>();
        granolaBarDescription.put("price", 50);
        granolaBarDescription.put("name", "Granola Bar");

        vendingMachine.addProduct(4, granolaBarDescription);
    }

    @Test
    public void test_workflow3_noProductName() {
        Map<String, Object> granolaBarDescription = new HashMap<>();
        granolaBarDescription.put("price", 50);

        vendingMachine.addProduct(4, granolaBarDescription);

        verifyStatic(VendingMachineHardwareFunctions.class, times(1));
        VendingMachineHardwareFunctions.showMessage("Product name is missing for product {price=50}");

        verifyNoMoreInteractions(VendingMachineHardwareFunctions.class);
    }
}
