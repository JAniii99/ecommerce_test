package com.ecommerce.utilities;

import com.ecommerce.base.DriverFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ScreenshotUtil - Captures browser screenshots on test failure.
 *
 * Why capture screenshots?
 * - Visual evidence of what went wrong
 * - Helps debug failures in CI/CD where you can't see the browser
 * - Required in professional test reports
 */
public class ScreenshotUtil {

    // Folder where screenshots are saved (from config.properties)
    private static final String SCREENSHOT_DIR =
            ConfigReader.get("screenshot.path");

    /**
     * Captures a screenshot and saves it to the screenshots/ folder.
     *
     * @param testName - used as the filename so you know which test failed
     * @return String  - the full file path of the saved screenshot
     */
    public static String captureScreenshot(String testName) {
        WebDriver driver = DriverFactory.getDriver();

        // TakesScreenshot is an interface implemented by ChromeDriver
        // It captures the current browser window as an image
        TakesScreenshot ts = (TakesScreenshot) driver;

        // getScreenshotAs() returns the screenshot as a temporary File
        File sourceFile = ts.getScreenshotAs(OutputType.FILE);

        // Create a unique filename using test name + timestamp
        // Example: testValidLogin_2026-06-15_00-30-45.png
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String fileName = testName + "_" + timestamp + ".png";

        // Full path where screenshot will be saved
        String filePath = SCREENSHOT_DIR + fileName;

        try {
            // FileUtils.copyFile() copies temp screenshot to our folder
            // This is why we added commons-io dependency in pom.xml
            FileUtils.copyFile(sourceFile, new File(filePath));
            System.out.println("📸 Screenshot saved: " + filePath);
        } catch (IOException e) {
            System.out.println("❌ Failed to save screenshot: " + e.getMessage());
        }

        return filePath;
    }
}