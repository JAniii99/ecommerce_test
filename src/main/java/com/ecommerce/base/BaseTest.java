package com.ecommerce.base;

import com.ecommerce.utilities.ConfigReader;
import com.ecommerce.utilities.ExtentReportManager;
import com.ecommerce.utilities.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

/**
 * BaseTest - Updated with Extent Reports integration.
 *
 * Lifecycle:
 * @BeforeSuite  → initialize report (once)
 * @BeforeMethod → open browser + create test node in report
 * @AfterMethod  → log result + capture screenshot if failed
 * @AfterSuite   → flush/save report to disk (once)
 */
public class BaseTest {

    protected WebDriver driver;

    /**
     * @BeforeSuite runs ONCE before any test in the entire suite.
     * Perfect place to initialize the report.
     */
    @BeforeSuite
    public void setUpSuite() {
        ExtentReportManager.initReports();
        System.out.println("🚀 Test Suite Started.");
    }

    /**
     * @BeforeMethod runs before EACH test.
     * Receives the Method object so we can get test name + description.
     *
     * @param method - TestNG injects current test method info
     */
    @BeforeMethod
    public void setUp(java.lang.reflect.Method method) {
        // Get test name and description from @Test annotation
        String testName = method.getName();
        String description = "";

        // Read description from @Test annotation if it exists
        org.testng.annotations.Test testAnnotation =
                method.getAnnotation(org.testng.annotations.Test.class);
        if (testAnnotation != null) {
            description = testAnnotation.description();
        }

        // Create a new node in the Extent Report for this test
        ExtentReportManager.createTest(testName, description);

        // Initialize browser
        driver = DriverFactory.getDriver();
        String baseUrl = ConfigReader.get("base.url");
        driver.get(baseUrl);

        // Log to Extent Report
        ExtentReportManager.getTest().info("🌐 Browser opened: " + baseUrl);
        System.out.println("✅ Browser opened and navigated to: " + baseUrl);
    }

    /**
     * @AfterMethod runs after EACH test.
     * Logs result to Extent Report and captures screenshot on failure.
     */
    @AfterMethod
    public void tearDown(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        if (result.getStatus() == ITestResult.SUCCESS) {
            // Log PASS to report
            ExtentReportManager.getTest().pass("✅ Test PASSED: " + testName);
            System.out.println("✅ Test PASSED: " + testName);

        } else if (result.getStatus() == ITestResult.FAILURE) {
            // Capture screenshot
            String screenshotPath = ScreenshotUtil.captureScreenshot(testName);

            // Log FAIL to report with failure reason
            ExtentReportManager.getTest()
                    .fail("❌ Test FAILED: " + testName)
                    .fail(result.getThrowable()) // logs the exception
                    .addScreenCaptureFromPath(   // embeds screenshot in report
                            "../../" + screenshotPath,
                            "Failure Screenshot"
                    );

            System.out.println("❌ Test FAILED: " + testName);

        } else if (result.getStatus() == ITestResult.SKIP) {
            // Log SKIP to report
            ExtentReportManager.getTest().skip("⚠️ Test SKIPPED: " + testName);
            System.out.println("⚠️ Test SKIPPED: " + testName);
        }

        // Always close browser after each test
        DriverFactory.quitDriver();
        System.out.println("🔄 Browser closed.");
    }

    /**
     * @AfterSuite runs ONCE after ALL tests complete.
     * Flushes/saves the HTML report to disk.
     */
    @AfterSuite
    public void tearDownSuite() {
        ExtentReportManager.flushReports();
        System.out.println("📊 Test Suite Completed. Report saved.");
    }
}