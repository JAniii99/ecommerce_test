package com.ecommerce.tests;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.CartPage;
import com.ecommerce.pages.CheckoutPage;
import com.ecommerce.pages.LoginPage;
import com.ecommerce.utilities.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * CheckoutTest - Test cases for Checkout Flow.
 *
 * TC011 - Proceed to Checkout (with login modal handling)
 * TC012 - Verify Order Summary and Complete Payment
 */
public class CheckoutTest extends BaseTest {

    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    private LoginPage loginPage;

    @BeforeMethod
    public void setUpPages() {
        cartPage     = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
        loginPage    = new LoginPage(driver);
    }

    /**
     * TC011 - Proceed to Checkout
     *
     * Steps:
     * 1. Login first
     * 2. Add product to cart
     * 3. View cart
     * 4. Click Proceed To Checkout
     * 5. Verify we land on Checkout page
     * 6. Verify delivery address is displayed
     *
     * Expected: Checkout page loads with address details
     */
    @Test(priority = 1, description = "TC011 - Verify Proceed to Checkout flow")
    public void testProceedToCheckout() {
        System.out.println("🧪 Running TC011 - Proceed to Checkout");

        // Step 1: Login first so we skip the login modal
        loginPage.loginWith(
                ConfigReader.get("valid.email"),
                ConfigReader.get("valid.password")
        );
        Assert.assertTrue(
                loginPage.isLoginSuccessful(),
                "❌ Login failed before checkout test!"
        );
        System.out.println("✅ Logged in successfully.");

        // Step 2: Add product to cart
        cartPage.navigateToProductsPage();
        cartPage.addFirstProductToCart();

        // Step 3: View cart
        cartPage.clickViewCart();

        // Step 4: Proceed to checkout
        checkoutPage.clickProceedToCheckout();

        // Step 5: Verify on checkout page
        Assert.assertTrue(
                checkoutPage.isOnCheckoutPage(),
                "❌ Not on Checkout page after clicking Proceed!"
        );

        // Step 6: Verify address is displayed
        Assert.assertTrue(
                checkoutPage.isDeliveryAddressDisplayed(),
                "❌ Delivery address not displayed on checkout page!"
        );

        System.out.println("✅ TC011 PASSED - Checkout page loaded with address!");
    }

    /**
     * TC012 - Verify Order Summary and Complete Payment
     *
     * Steps:
     * 1. Login
     * 2. Add product to cart
     * 3. Proceed to checkout
     * 4. Verify order summary shows correct product
     * 5. Add a comment
     * 6. Place Order
     * 7. Fill payment details
     * 8. Pay and Confirm
     * 9. Verify order placed successfully
     *
     * Expected: Complete checkout flow succeeds
     */
    @Test(priority = 2, description = "TC012 - Verify order summary and complete payment")
    public void testVerifyOrderSummaryAndPayment() {
        System.out.println("🧪 Running TC012 - Verify Order Summary and Payment");

        // Step 1: Login
        loginPage.loginWith(
                ConfigReader.get("valid.email"),
                ConfigReader.get("valid.password")
        );
        Assert.assertTrue(
                loginPage.isLoginSuccessful(),
                "❌ Login failed before checkout test!"
        );

        // Step 2: Add product to cart
        cartPage.navigateToProductsPage();
        cartPage.addFirstProductToCart();

        // Step 3: Go to cart and proceed to checkout
        cartPage.clickViewCart();
        checkoutPage.clickProceedToCheckout();

        // Step 4: Verify checkout page loaded
        Assert.assertTrue(
                checkoutPage.isOnCheckoutPage(),
                "❌ Not on Checkout page!"
        );

        // Step 5: Verify order summary contains our product
        java.util.List<String> products = checkoutPage.getOrderSummaryProducts();
        Assert.assertTrue(
                products.size() > 0,
                "❌ No products in order summary!"
        );
        System.out.println("📦 Order summary products: " + products);

        // Step 6: Add comment
        checkoutPage.addOrderComment("Test order - automation framework");
        System.out.println("💬 Comment added.");

        // Step 7: Place Order
        checkoutPage.clickPlaceOrder();
        System.out.println("🛒 Place Order clicked - navigating to payment.");

        // Step 8: Fill payment details (test data)
        checkoutPage.fillPaymentDetails(
                "Janitha Test",   // name on card
                "4111111111111111", // test card number (Visa test number)
                "123",              // CVC
                "12",               // expiry month
                "2028"              // expiry year
        );

        // Step 9: Pay and Confirm
        checkoutPage.clickPayAndConfirm();

        // Step 10: Verify order placed
        Assert.assertTrue(
                checkoutPage.isOrderPlacedSuccessfully(),
                "❌ Order confirmation not shown after payment!"
        );

        System.out.println("✅ TC012 PASSED - Order placed successfully!");
    }
}