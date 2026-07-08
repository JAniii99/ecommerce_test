package com.ecommerce.tests;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.RegistrationPage;
import com.ecommerce.utilities.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * RegistrationTest - Test cases for User Registration feature.
 *
 * TC004 - Successful new user registration
 * TC005 - Registration attempt with existing email
 */
public class RegistrationTest extends BaseTest {

    private RegistrationPage registrationPage;

    @BeforeMethod
    public void setUpRegistrationPage() {
        registrationPage = new RegistrationPage(driver);
    }

    /**
     * TC004 - Successful Registration
     *
     * Steps:
     * 1. Navigate to Signup page
     * 2. Enter unique name and email
     * 3. Click Signup
     * 4. Fill account details form
     * 5. Click Create Account
     * 6. Verify "Account Created!" confirmation
     *
     * Expected: Account created successfully
     */
    @Test(priority = 1, description = "TC004 - Verify successful new user registration")
    public void testSuccessfulRegistration() {
        System.out.println("🧪 Running TC004 - Successful Registration");

        // Navigate to signup page
        registrationPage.navigateToSignupPage();

        // Generate unique email so test can run multiple times
        // Same email twice would fail because account already exists
        String uniqueEmail = RegistrationPage.generateUniqueEmail();
        String name = ConfigReader.get("register.name");

        // Step 1: Fill initial signup form
        registrationPage.enterSignupDetails(name, uniqueEmail);
        registrationPage.clickSignupButton();

        // Verify we landed on Account Information page
        Assert.assertTrue(
                registrationPage.isOnAccountInfoPage(),
                "❌ Should be on Account Information page after signup!"
        );

        // Step 2: Fill detailed account information
        registrationPage.fillAccountDetails(
                ConfigReader.get("register.password")
        );

        // Step 3: Submit registration
        registrationPage.clickCreateAccount();

        // Step 4: Verify success
        Assert.assertTrue(
                registrationPage.isAccountCreatedSuccessfully(),
                "❌ Account Created! confirmation not shown."
        );

        System.out.println("✅ TC004 PASSED - New user registered successfully!");
    }

    /**
     * TC005 - Existing Email Registration
     *
     * Steps:
     * 1. Navigate to Signup page
     * 2. Enter name and ALREADY REGISTERED email
     * 3. Click Signup
     * 4. Verify error message "Email Address already exist!"
     *
     * Expected: Error message shown, cannot register duplicate email
     */
    @Test(priority = 2, description = "TC005 - Verify error when registering with existing email")
    public void testRegistrationWithExistingEmail() {
        System.out.println("🧪 Running TC005 - Existing Email Registration");

        registrationPage.navigateToSignupPage();

        // Use the already-registered email from config
        registrationPage.enterSignupDetails(
                ConfigReader.get("register.name"),
                ConfigReader.get("register.existing.email")
        );
        registrationPage.clickSignupButton();

        // Verify error message appears
        Assert.assertTrue(
                registrationPage.isEmailAlreadyExistsErrorShown(),
                "❌ Error message not shown for existing email!"
        );

        System.out.println("✅ TC005 PASSED - Existing email correctly rejected!");
    }
}