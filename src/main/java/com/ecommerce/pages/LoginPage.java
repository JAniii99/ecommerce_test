package com.ecommerce.pages;

import com.ecommerce.utilities.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * LoginPage - Page Object for the Login page.
 *
 * This class represents ONE page in your application.
 * It contains:
 * 1. Locators  - HOW to find elements on the page
 * 2. Actions   - WHAT you can do on the page (click, type, etc.)
 * 3. Getters   - HOW to read information from the page
 *
 * Tests NEVER interact with elements directly.
 * Tests call METHODS on this class instead.
 */
public class LoginPage {

    // ==================== FIELDS ====================

    // WebDriver instance passed in from the test
    private WebDriver driver;

    // WebDriverWait for Explicit Waits
    // Explicit wait = wait for a SPECIFIC condition on a SPECIFIC element
    // Much better than Thread.sleep() which always waits full duration
    private WebDriverWait wait;

    // ==================== LOCATORS ====================
    // By objects define HOW to find elements
    // Stored as constants so they're easy to update if UI changes
    // Private because only THIS class should use these locators

    // Login form elements
    private By emailField      = By.xpath("//input[@data-qa='login-email']");
    private By passwordField   = By.xpath("//input[@data-qa='login-password']");
    private By loginButton     = By.xpath("//button[@data-qa='login-button']");

    // Navigation
    private By signupLoginLink = By.xpath("//a[normalize-space()='Signup / Login']");

    // Result indicators
    private By errorMessage    = By.xpath("//p[contains(text(),'Your email or password is incorrect')]");
    private By loggedInAsText  = By.xpath("//a[contains(text(),'Logged in as')]");

    // ==================== CONSTRUCTOR ====================

    /**
     * Constructor receives WebDriver from the test class.
     * This is called DEPENDENCY INJECTION - we inject the driver
     * rather than creating it here. This keeps DriverFactory
     * as the single place responsible for driver creation.
     *
     * @param driver - the WebDriver instance from BaseTest
     */
    public LoginPage(WebDriver driver) {
        this.driver = driver;

        // Initialize explicit wait with timeout from config
        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(ConfigReader.getInt("explicit.wait"))
        );
    }

    // ==================== ACTION METHODS ====================

    /**
     * Navigates to the Login page by clicking Signup/Login link.
     * Always navigate explicitly - don't assume you're on the right page.
     */
    public void navigateToLoginPage() {
        // Wait until the link is clickable, then click it
        wait.until(ExpectedConditions.elementToBeClickable(signupLoginLink)).click();
        System.out.println("📍 Navigated to Login page.");
    }

    /**
     * Types email address into the email input field.
     * Clears field first to avoid appending to existing text.
     *
     * @param email - email address to enter
     */
    public void enterEmail(String email) {
        // Wait until element is visible before interacting
        WebElement field = wait.until(
                ExpectedConditions.visibilityOfElementLocated(emailField)
        );
        field.clear();           // Clear any existing text first
        field.sendKeys(email);   // Type the email
        System.out.println("📧 Entered email: " + email);
    }

    /**
     * Types password into the password input field.
     *
     * @param password - password to enter
     */
    public void enterPassword(String password) {
        WebElement field = wait.until(
                ExpectedConditions.visibilityOfElementLocated(passwordField)
        );
        field.clear();
        field.sendKeys(password);
        System.out.println("🔑 Entered password.");
    }

    /**
     * Clicks the Login button to submit the form.
     */
    public void clickLoginButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
        System.out.println("🖱️ Clicked Login button.");
    }

    /**
     * Combines all login steps into ONE method.
     * Use this when you just want to login quickly
     * without asserting each individual step.
     *
     * @param email    - user email
     * @param password - user password
     */
    public void loginWith(String email, String password) {
        navigateToLoginPage();
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
    }

    // ==================== GETTER METHODS ====================
    // These methods READ information from the page
    // Used in tests for assertions

    /**
     * Checks if login was successful by looking for "Logged in as" text.
     *
     * @return true if user is logged in, false otherwise
     */
    public boolean isLoginSuccessful() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(loggedInAsText));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if error message is displayed after failed login.
     *
     * @return true if error message is visible, false otherwise
     */
    public boolean isErrorMessageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the error message text for assertion in tests.
     *
     * @return the error message string
     */
    public String getErrorMessage() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(errorMessage)
        ).getText();
    }
}