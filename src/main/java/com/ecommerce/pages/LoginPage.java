package com.ecommerce.pages;

import com.ecommerce.utilities.AdHandler;
import com.ecommerce.utilities.ConfigReader;
import com.ecommerce.utilities.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private static final Logger log = LoggerUtil.getLogger(LoginPage.class);

    private WebDriver driver;
    private WebDriverWait wait;

    private By emailField      = By.xpath("//input[@data-qa='login-email']");
    private By passwordField   = By.xpath("//input[@data-qa='login-password']");
    private By loginButton     = By.xpath("//button[@data-qa='login-button']");
    private By signupLoginLink = By.xpath("//a[normalize-space()='Signup / Login']");
    private By errorMessage    = By.xpath("//p[contains(text(),'Your email or password is incorrect')]");
    private By loggedInAsText  = By.xpath("//a[contains(text(),'Logged in as')]");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(ConfigReader.getInt("explicit.wait"))
        );
        log.debug("LoginPage initialized.");
    }

    public void navigateToLoginPage() {
        log.info("Navigating to Login page.");
        wait.until(ExpectedConditions.elementToBeClickable(signupLoginLink)).click();
    }

    public void enterEmail(String email) {
        log.info("Entering email: {}", email);
        WebElement field = wait.until(
                ExpectedConditions.visibilityOfElementLocated(emailField)
        );
        field.clear();
        field.sendKeys(email);
    }

    public void enterPassword(String password) {
        log.info("Entering password.");
        WebElement field = wait.until(
                ExpectedConditions.visibilityOfElementLocated(passwordField)
        );
        field.clear();
        field.sendKeys(password);
    }

    public void clickLoginButton() {
        log.info("Clicking Login button.");
        // Remove ads before clicking - ads sometimes cover the login button
        AdHandler.closeAds(driver);
        WebElement btn = wait.until(
                ExpectedConditions.elementToBeClickable(loginButton)
        );
        AdHandler.jsClick(driver, btn);
        log.info("Clicked Login button via JS.");
    }

    public void loginWith(String email, String password) {
        log.info("Performing login with email: {}", email);
        navigateToLoginPage();
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
    }

    public boolean isLoginSuccessful() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(loggedInAsText));
            log.info("Login successful - 'Logged in as' text found.");
            return true;
        } catch (Exception e) {
            log.warn("Login unsuccessful - 'Logged in as' text not found.");
            return false;
        }
    }

    public boolean isErrorMessageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            log.info("Error message displayed as expected.");
            return true;
        } catch (Exception e) {
            log.warn("Error message not found.");
            return false;
        }
    }

    public String getErrorMessage() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(errorMessage)
        ).getText();
    }
}