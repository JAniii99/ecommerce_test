package com.ecommerce.base;

import com.ecommerce.utilities.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * BaseTest - Parent class for ALL test classes in the framework.
 *
 * Every test class EXTENDS this class to inherit:
 * - Browser setup (@BeforeMethod)
 * - Browser teardown (@AfterMethod)
 * - Access to WebDriver instance
 *
 * This follows the DRY principle (Don't Repeat Yourself).
 * Setup/teardown code is written ONCE here, not in every test.
 */
public class BaseTest {

    /**
     * 'driver' is protected so child test classes can access it directly.
     *
     * private   → only this class can use it (too restrictive)
     * protected → this class AND subclasses can use it (perfect)
     * public    → everyone can use it (too open)
     */
    protected WebDriver driver;

    /**
     * @BeforeMethod - TestNG annotation that runs this method
     * BEFORE EVERY @Test method in the test class.
     *
     * Flow for each test:
     * setUp() → @Test method runs → tearDown()
     */
    @BeforeMethod
    public void setUp() {
        // Get the driver from DriverFactory
        // This creates a NEW browser instance if one doesn't exist
        driver = DriverFactory.getDriver();

        // Navigate to the base URL from config.properties
        String baseUrl = ConfigReader.get("base.url");
        driver.get(baseUrl);

        System.out.println("✅ Browser opened and navigated to: " + baseUrl);
    }

    /**
     * @AfterMethod - TestNG annotation that runs this method
     * AFTER EVERY @Test method, whether the test PASSED or FAILED.
     *
     * This guarantees browser is always closed, even if test throws exception.
     * Without this, failed tests would leave browsers open forever.
     */
    @AfterMethod
    public void tearDown() {
        System.out.println("🔄 Closing browser after test...");
        DriverFactory.quitDriver();
    }
}