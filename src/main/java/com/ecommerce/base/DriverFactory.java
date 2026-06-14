package com.ecommerce.base;

import com.ecommerce.utilities.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

/**
 * DriverFactory - Manages WebDriver instance lifecycle.
 *
 * Uses ThreadLocal to store the driver, which means each thread
 * gets its OWN separate driver instance.
 * This is CRITICAL for parallel test execution later.
 */
public class DriverFactory {

    /**
     * ThreadLocal wraps the WebDriver so each thread has its own copy.
     *
     * Why ThreadLocal?
     * Imagine running 3 tests at the same time (parallel execution).
     * Without ThreadLocal: all 3 tests share ONE driver → chaos, tests interfere.
     * With ThreadLocal: each test gets its OWN driver → safe, isolated.
     *
     * Even if you run tests sequentially now, using ThreadLocal from the
     * start is an industry best practice — future-proofs your framework.
     */
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    /**
     * Creates and returns a WebDriver instance.
     * Reads browser type from config.properties.
     *
     * @return WebDriver - the browser instance ready to use
     */
    public static WebDriver getDriver() {

        // Only create a new driver if one doesn't exist for this thread
        // Prevents creating multiple browser windows accidentally
        if (driver.get() == null) {
            String browser = ConfigReader.get("browser").toLowerCase();

            switch (browser) {
                case "chrome":
                    initChromeDriver();
                    break;
                // Easy to add Firefox, Edge later:
                // case "firefox": initFirefoxDriver(); break;
                default:
                    throw new RuntimeException(
                            "❌ Browser '" + browser + "' is not supported. " +
                                    "Check browser= value in config.properties"
                    );
            }

            // Configure timeouts ONCE after driver is created
            configureTimeouts();
        }

        return driver.get();
    }

    /**
     * Sets up ChromeDriver using WebDriverManager.
     * WebDriverManager automatically downloads the correct ChromeDriver
     * version that matches your installed Chrome browser.
     * No more manual driver downloads!
     */
    private static void initChromeDriver() {
        // WebDriverManager downloads correct ChromeDriver automatically
        WebDriverManager.chromedriver().setup();

        // ChromeOptions lets us customize browser behavior
        ChromeOptions options = new ChromeOptions();

        // Read headless setting from config
        // headless=true means browser runs invisibly (good for CI/CD)
        // headless=false means you see the browser open (good for development)
        if (ConfigReader.getBoolean("headless")) {
            options.addArguments("--headless=new"); // "new" is modern headless mode
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
        }

        // These arguments make Chrome more stable during automation
        options.addArguments("--start-maximized");         // Open maximized
        options.addArguments("--disable-notifications");   // Block popups
        options.addArguments("--disable-extensions");      // No extensions

        // Create the ChromeDriver with our options
        WebDriver chromeDriver = new ChromeDriver(options);

        // Store it in ThreadLocal for this thread
        driver.set(chromeDriver);

        System.out.println("✅ ChromeDriver initialized successfully.");
    }

    /**
     * Configures browser timeouts from config.properties.
     *
     * implicitWait: How long Selenium waits for an element before throwing
     *               NoSuchElementException. Applied globally to all findElement calls.
     *
     * pageLoadTimeout: How long to wait for a page to fully load.
     *                  Throws TimeoutException if exceeded.
     */
    private static void configureTimeouts() {
        WebDriver currentDriver = driver.get();

        // Implicit wait - global wait for element finding
        currentDriver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(ConfigReader.getInt("implicit.wait"))
        );

        // Page load timeout - how long to wait for pages to load
        currentDriver.manage().timeouts().pageLoadTimeout(
                Duration.ofSeconds(ConfigReader.getInt("page.load.timeout"))
        );

        // Maximize browser window for consistent element visibility
        currentDriver.manage().window().maximize();

        System.out.println("✅ Browser timeouts configured.");
    }

    /**
     * Closes the browser and removes the driver from ThreadLocal.
     *
     * IMPORTANT: Always call this after tests finish.
     * Not calling quit() leaves browser processes running in background
     * which wastes memory and can cause issues in CI/CD.
     */
    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();   // Closes ALL browser windows and kills process
            driver.remove();       // Removes from ThreadLocal - prevents memory leak
            System.out.println("✅ Browser closed successfully.");
        }
    }
}