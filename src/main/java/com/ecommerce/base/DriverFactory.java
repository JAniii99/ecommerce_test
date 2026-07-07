package com.ecommerce.base;

import com.ecommerce.utilities.ConfigReader;
import com.ecommerce.utilities.LoggerUtil;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public class DriverFactory {

    private static final Logger log = LoggerUtil.getLogger(DriverFactory.class);
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            String browser = ConfigReader.get("browser").toLowerCase();
            log.info("Initializing browser: {}", browser);

            switch (browser) {
                case "chrome":
                    initChromeDriver();
                    break;
                default:
                    log.error("Unsupported browser: {}", browser);
                    throw new RuntimeException("Browser not supported: " + browser);
            }
            configureTimeouts();
        }
        return driver.get();
    }

    private static void initChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        if (ConfigReader.getBoolean("headless")) {
            log.info("Running in headless mode.");
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--disable-dev-shm-usage");
        }

        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-extensions");

        driver.set(new ChromeDriver(options));
        log.info("ChromeDriver initialized successfully.");
    }

    private static void configureTimeouts() {
        driver.get().manage().timeouts().implicitlyWait(
                Duration.ofSeconds(ConfigReader.getInt("implicit.wait"))
        );
        driver.get().manage().timeouts().pageLoadTimeout(
                Duration.ofSeconds(ConfigReader.getInt("page.load.timeout"))
        );
        driver.get().manage().window().maximize();
        log.info("Browser timeouts configured.");
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
            log.info("Browser closed and driver removed from ThreadLocal.");
        }
    }
}