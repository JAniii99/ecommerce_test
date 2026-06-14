package com.ecommerce.tests;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.LoginPage;
import com.ecommerce.utilities.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * LoginTest - Contains all test cases for Login functionality.
 *
 * Extends BaseTest to inherit:
 * - @BeforeMethod: opens browser + navigates to base URL
 * - @AfterMethod: closes browser
 * - protected WebDriver driver: ready to use
 *
 * Test Cases:
 * TC001 - Valid Login
 * TC002 - Invalid Login (wrong password)
 * TC003 - Empty Credentials
 */
public class LoginTest extends BaseTest {

    // Page Object - our interface to the login page
    private LoginPage loginPage;

    /**
     * This @BeforeMethod runs AFTER BaseTest's @BeforeMethod.
     * By the time this runs, driver is already initialized
     * and browser is already open on the base URL.
     *
     * We just need to initialize our Page Object here.
     */
    @BeforeMethod
    public void setUpLoginPage() {
        // Pass the driver (inherited from BaseTest) into LoginPage
        loginPage = new LoginPage(driver);
    }

    /**
     * TC001 - Valid Login Test
     *
     * Steps:
     * 1. Navigate to login page
     * 2. Enter valid email and password
     * 3. Click login
     * 4. Verify "Logged in as" text is visible
     *
     * Expected Result: User is logged in successfully
     */
    @Test(priority = 1, description = "TC001 - Verify successful login with valid credentials")
    public void testValidLogin() {
        System.out.println("🧪 Running TC001 - Valid Login");

        // Read credentials from config.properties
        // NEVER hardcode credentials in test code
        String email    = ConfigReader.get("valid.email");
        String password = ConfigReader.get("valid.password");

        // Perform login using Page Object methods
        loginPage.loginWith(email, password);

        // ASSERTION - this is what makes it a TEST, not just a script
        // Assert.assertTrue() fails the test if condition is false
        Assert.assertTrue(
                loginPage.isLoginSuccessful(),
                "❌ Login failed! 'Logged in as' text not found after valid login."
        );

        System.out.println("✅ TC001 PASSED - Valid login successful!");
    }

    /**
     * TC002 - Invalid Login Test
     *
     * Steps:
     * 1. Navigate to login page
     * 2. Enter valid email but WRONG password
     * 3. Click login
     * 4. Verify error message is displayed
     *
     * Expected Result: Error message shown, user NOT logged in
     */
    @Test(priority = 2, description = "TC002 - Verify error message with invalid credentials")
    public void testInvalidLogin() {
        System.out.println("🧪 Running TC002 - Invalid Login");

        // Use real email but wrong password
        loginPage.loginWith(
                ConfigReader.get("valid.email"),
                "WrongPassword@999"
        );

        // Assert error message is displayed
        Assert.assertTrue(
                loginPage.isErrorMessageDisplayed(),
                "❌ Error message not displayed for invalid credentials!"
        );

        System.out.println("✅ TC002 PASSED - Error message displayed correctly!");
    }

    /**
     * TC003 - Empty Credentials Test
     *
     * Steps:
     * 1. Navigate to login page
     * 2. Leave email and password EMPTY
     * 3. Click login
     * 4. Verify user is NOT logged in
     *
     * Expected Result: Login does not succeed with empty fields
     */
    @Test(priority = 3, description = "TC003 - Verify login fails with empty credentials")
    public void testEmptyCredentials() {
        System.out.println("🧪 Running TC003 - Empty Credentials");

        // Submit form with empty credentials
        loginPage.loginWith("", "");

        // Assert login did NOT succeed
        Assert.assertFalse(
                loginPage.isLoginSuccessful(),
                "❌ Login should NOT succeed with empty credentials!"
        );

        System.out.println("✅ TC003 PASSED - Empty credentials correctly rejected!");
    }
}