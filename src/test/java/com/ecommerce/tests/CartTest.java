package com.ecommerce.tests;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.CartPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * CartTest - Test cases for Add to Cart feature.
 *
 * TC008 - Add single product to cart
 * TC009 - Add multiple products to cart
 * TC010 - Verify cart quantity
 */
public class CartTest extends BaseTest {

    private CartPage cartPage;

    @BeforeMethod
    public void setUpCartPage() {
        cartPage = new CartPage(driver);
    }

    /**
     * TC008 - Add Single Product to Cart
     *
     * Steps:
     * 1. Navigate to Products page
     * 2. Add first product to cart
     * 3. Verify "Added!" modal appears
     * 4. Click View Cart
     * 5. Verify cart has exactly 1 item
     * 6. Verify "Blue Top" is in cart
     *
     * Expected: Single product added successfully
     */
    @Test(priority = 1, description = "TC008 - Verify single product can be added to cart")
    public void testAddSingleProductToCart() {
        System.out.println("🧪 Running TC008 - Add Single Product to Cart");

        // Step 1: Go to Products page
        cartPage.navigateToProductsPage();

        // Step 2: Add first product
        cartPage.addFirstProductToCart();

        // Step 3: Verify modal appeared
        Assert.assertTrue(
                cartPage.isAddedToCartModalVisible(),
                "❌ 'Added!' modal not visible after adding product!"
        );

        // Step 4: Go to cart
        cartPage.clickViewCart();

        // Step 5: Verify cart has 1 item
        int itemCount = cartPage.getCartItemCount();
        Assert.assertEquals(
                itemCount, 1,
                "❌ Expected 1 item in cart but found: " + itemCount
        );

        // Step 6: Verify correct product
        Assert.assertTrue(
                cartPage.isProductInCart("Blue Top"),
                "❌ 'Blue Top' not found in cart!"
        );

        System.out.println("✅ TC008 PASSED - Single product added to cart!");
    }

    /**
     * TC009 - Add Multiple Products to Cart
     *
     * Steps:
     * 1. Navigate to Products page
     * 2. Add first product → Continue Shopping
     * 3. Add second product → View Cart
     * 4. Verify cart has exactly 2 items
     *
     * Expected: Two different products in cart
     */
    @Test(priority = 2, description = "TC009 - Verify multiple products can be added to cart")
    public void testAddMultipleProductsToCart() {
        System.out.println("🧪 Running TC009 - Add Multiple Products to Cart");

        // Step 1: Go to Products page
        cartPage.navigateToProductsPage();

        // Step 2: Add FIRST product → Continue Shopping
        cartPage.addProductToCartByIndex(0);
        Assert.assertTrue(
                cartPage.isAddedToCartModalVisible(),
                "❌ Modal not visible after adding first product!"
        );
        cartPage.clickContinueShopping(); // Stay on products page

        // Step 3: Add SECOND product → View Cart
        cartPage.addProductToCartByIndex(1);
        Assert.assertTrue(
                cartPage.isAddedToCartModalVisible(),
                "❌ Modal not visible after adding second product!"
        );
        cartPage.clickViewCart(); // Go to cart

        // Step 4: Verify 2 items in cart
        int itemCount = cartPage.getCartItemCount();
        Assert.assertEquals(
                itemCount, 2,
                "❌ Expected 2 items in cart but found: " + itemCount
        );

        System.out.println("✅ TC009 PASSED - " + itemCount + " products in cart!");
        System.out.println("🛒 Cart contents: " + cartPage.getAllCartProductNames());
    }

    /**
     * TC010 - Verify Cart Quantity
     *
     * Steps:
     * 1. Navigate to Products page
     * 2. Add first product to cart
     * 3. View Cart
     * 4. Verify quantity shows as 1
     *
     * Expected: Quantity = 1 for newly added product
     */
    @Test(priority = 3, description = "TC010 - Verify product quantity is correct in cart")
    public void testVerifyCartQuantity() {
        System.out.println("🧪 Running TC010 - Verify Cart Quantity");

        // Step 1: Go to Products page
        cartPage.navigateToProductsPage();

        // Step 2: Add first product
        cartPage.addFirstProductToCart();

        // Step 3: Go to cart
        cartPage.clickViewCart();

        // Step 4: Verify quantity is 1
        int quantity = cartPage.getProductQuantityInCart(0);
        Assert.assertEquals(
                quantity, 1,
                "❌ Expected quantity 1 but found: " + quantity
        );

        System.out.println("✅ TC010 PASSED - Cart quantity verified: " + quantity);
    }
}