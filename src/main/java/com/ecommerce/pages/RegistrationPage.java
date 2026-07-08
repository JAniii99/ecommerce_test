package com.ecommerce.pages;

import com.ecommerce.utilities.ConfigReader;
import com.ecommerce.utilities.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.UUID;

/**
 * RegistrationPage - Page Object for the Registration flow.
 *
 * Covers TWO pages:
 * Page 1: Signup form (name + email) on Login/Signup page
 * Page 2: Account details form (password, DOB, address etc.)
 */
public class RegistrationPage {

    private static final Logger log = LoggerUtil.getLogger(RegistrationPage.class);

    private WebDriver driver;
    private WebDriverWait wait;

    // ==================== PAGE 1 LOCATORS ====================
    // Signup section on the Login/Signup page

    private By signupLoginLink  = By.xpath("//a[normalize-space()='Signup / Login']");
    private By signupNameField  = By.xpath("//input[@data-qa='signup-name']");
    private By signupEmailField = By.xpath("//input[@data-qa='signup-email']");
    private By signupButton     = By.xpath("//button[@data-qa='signup-button']");

    // Error shown when email already exists
    private By emailExistsError = By.xpath(
            "//p[contains(text(),'Email Address already exist!')]"
    );

    // ==================== PAGE 2 LOCATORS ====================
    // Account Information form (shown after clicking Signup)

    private By pageTitleText      = By.xpath("//b[contains(text(),'Enter Account Information')]");
    private By titleMrRadio       = By.xpath("//input[@id='id_gender1']");
    private By passwordField      = By.xpath("//input[@data-qa='password']");
    private By birthDayDropdown   = By.xpath("//select[@data-qa='days']");
    private By birthMonthDropdown = By.xpath("//select[@data-qa='months']");
    private By birthYearDropdown  = By.xpath("//select[@data-qa='years']");
    private By firstNameField     = By.xpath("//input[@data-qa='first_name']");
    private By lastNameField      = By.xpath("//input[@data-qa='last_name']");
    private By addressField       = By.xpath("//input[@data-qa='address']");
    private By countryDropdown    = By.xpath("//select[@data-qa='country']");
    private By stateField         = By.xpath("//input[@data-qa='state']");
    private By cityField          = By.xpath("//input[@data-qa='city']");
    private By zipcodeField       = By.xpath("//input[@data-qa='zipcode']");
    private By mobileField        = By.xpath("//input[@data-qa='mobile_number']");
    private By createAccountBtn   = By.xpath("//button[@data-qa='create-account']");

    // ==================== PAGE 3 LOCATORS ====================
    // Confirmation page after successful registration

    private By accountCreatedText = By.xpath(
            "//b[contains(text(),'Account Created!')]"
    );
    private By continueButton     = By.xpath("//a[@data-qa='continue-button']");

    // ==================== CONSTRUCTOR ====================

    public RegistrationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(ConfigReader.getInt("explicit.wait"))
        );
        log.debug("RegistrationPage initialized.");
    }

    // ==================== PAGE 1 ACTIONS ====================

    /**
     * Navigates to the Signup/Login page.
     */
    public void navigateToSignupPage() {
        log.info("Navigating to Signup/Login page.");
        wait.until(ExpectedConditions.elementToBeClickable(signupLoginLink)).click();
    }

    /**
     * Fills in the initial signup form (name + email) and clicks Signup.
     *
     * @param name  - user's full name
     * @param email - user's email address
     */
    public void enterSignupDetails(String name, String email) {
        log.info("Entering signup details - Name: {}, Email: {}", name, email);

        WebElement nameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(signupNameField)
        );
        nameInput.clear();
        nameInput.sendKeys(name);

        WebElement emailInput = driver.findElement(signupEmailField);
        emailInput.clear();
        emailInput.sendKeys(email);
    }

    /**
     * Clicks the Signup button to proceed to account details page.
     */
    public void clickSignupButton() {
        log.info("Clicking Signup button.");
        wait.until(ExpectedConditions.elementToBeClickable(signupButton)).click();
    }

    // ==================== PAGE 2 ACTIONS ====================

    /**
     * Checks if we landed on the "Enter Account Information" page.
     * Used to verify Step 1 was successful.
     */
    public boolean isOnAccountInfoPage() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitleText));
            log.info("Successfully landed on Account Information page.");
            return true;
        } catch (Exception e) {
            log.warn("Account Information page not found.");
            return false;
        }
    }

    /**
     * Fills in all account details on Page 2.
     * Uses hardcoded test data for non-critical fields.
     *
     * @param password - account password
     */
    public void fillAccountDetails(String password) {
        log.info("Filling account details form.");

        // Select title (Mr/Mrs radio button)
        wait.until(ExpectedConditions.elementToBeClickable(titleMrRadio)).click();

        // Enter password
        WebElement pass = wait.until(
                ExpectedConditions.visibilityOfElementLocated(passwordField)
        );
        pass.sendKeys(password);

        // Select Date of Birth using Select class for dropdowns
        new Select(driver.findElement(birthDayDropdown)).selectByValue("15");
        new Select(driver.findElement(birthMonthDropdown)).selectByValue("6");
        new Select(driver.findElement(birthYearDropdown)).selectByValue("1995");

        // Fill address details
        driver.findElement(firstNameField).sendKeys("Janitha");
        driver.findElement(lastNameField).sendKeys("Test");
        driver.findElement(addressField).sendKeys("123 Test Street");
        new Select(driver.findElement(countryDropdown)).selectByVisibleText("United States");
        driver.findElement(stateField).sendKeys("California");
        driver.findElement(cityField).sendKeys("Los Angeles");
        driver.findElement(zipcodeField).sendKeys("90001");
        driver.findElement(mobileField).sendKeys("1234567890");

        log.info("Account details form filled successfully.");
    }

    /**
     * Clicks the "Create Account" button to submit the registration.
     */
    public void clickCreateAccount() {
        log.info("Clicking Create Account button.");
        wait.until(ExpectedConditions.elementToBeClickable(createAccountBtn)).click();
    }

    // ==================== PAGE 3 ACTIONS ====================

    /**
     * Verifies "Account Created!" confirmation is shown.
     *
     * @return true if account was created successfully
     */
    public boolean isAccountCreatedSuccessfully() {
        try {
            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(accountCreatedText)
            );
            log.info("Account Created! confirmation found.");
            return true;
        } catch (Exception e) {
            log.warn("Account Created! confirmation NOT found.");
            return false;
        }
    }

    /**
     * Clicks Continue after account creation.
     */
    public void clickContinue() {
        log.info("Clicking Continue button after registration.");
        wait.until(ExpectedConditions.elementToBeClickable(continueButton)).click();
    }

    // ==================== ERROR CHECKS ====================

    /**
     * Checks if "Email Address already exist!" error is shown.
     * Used for the existing user registration test.
     *
     * @return true if error message is visible
     */
    public boolean isEmailAlreadyExistsErrorShown() {
        try {
            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(emailExistsError)
            );
            log.info("Email already exists error message displayed.");
            return true;
        } catch (Exception e) {
            log.warn("Email already exists error NOT found.");
            return false;
        }
    }

    // ==================== HELPER METHODS ====================

    /**
     * Generates a unique email address for each test run.
     * Why? Because once an email is registered, it can't be used again.
     * UUID generates a random unique string every time.
     *
     * Example output: test_a3f2b1c4@testmail.com
     *
     * @return unique email string
     */
    public static String generateUniqueEmail() {
        String unique = UUID.randomUUID().toString().substring(0, 8);
        String email = "test_" + unique + "@testmail.com";
        LoggerUtil.getLogger(RegistrationPage.class)
                .info("Generated unique email: {}", email);
        return email;
    }
}