package com.ecommerce.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ExtentReportManager - Creates and manages the Extent HTML report.
 *
 * Uses two key Extent classes:
 * - ExtentReports    → the overall report (one per test run)
 * - ExtentTest       → one entry per test method (pass/fail/skip)
 *
 * Uses ThreadLocal<ExtentTest> so each thread has its own test
 * entry — safe for parallel execution.
 */
public class ExtentReportManager {

    // The main report object - one per test run
    private static ExtentReports extent;

    // ThreadLocal ensures each test thread has its own ExtentTest node
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    /**
     * Creates and configures the ExtentReports instance.
     * Called ONCE at the start of the test suite (@BeforeSuite).
     */
    public static void initReports() {
        // Generate unique report filename with timestamp
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String reportPath = ConfigReader.get("report.path")
                + "TestReport_" + timestamp + ".html";

        // ExtentSparkReporter generates the HTML file
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);

        // Configure report appearance
        sparkReporter.config().setDocumentTitle("E-Commerce Automation Report");
        sparkReporter.config().setReportName("Selenium TestNG Framework");
        sparkReporter.config().setTheme(Theme.DARK);  // Dark theme looks professional
        sparkReporter.config().setEncoding("UTF-8");

        // Initialize ExtentReports and attach the HTML reporter
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        // Add system information shown in report header
        extent.setSystemInfo("Project", "E-Commerce Automation");
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("Browser", ConfigReader.get("browser"));
        extent.setSystemInfo("Base URL", ConfigReader.get("base.url"));
        extent.setSystemInfo("Tester", "Janitha");
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));

        System.out.println("✅ Extent Report initialized: " + reportPath);
    }

    /**
     * Creates a new test node in the report for a specific test method.
     * Called at the START of each test (@BeforeMethod).
     *
     * @param testName    - name of the test method
     * @param description - description from @Test annotation
     */
    public static void createTest(String testName, String description) {
        // createTest() adds a new row/entry to the HTML report
        ExtentTest test = extent.createTest(testName, description);

        // Store in ThreadLocal so this thread can access it later
        extentTest.set(test);
    }

    /**
     * Gets the ExtentTest for the current thread.
     * Used by BaseTest to log pass/fail/info messages.
     *
     * @return ExtentTest for current test
     */
    public static ExtentTest getTest() {
        return extentTest.get();
    }

    /**
     * Writes the report to disk.
     * MUST be called at end of suite, otherwise report file is empty.
     * Called @AfterSuite.
     */
    public static void flushReports() {
        if (extent != null) {
            extent.flush(); // Writes all test results to the HTML file
            System.out.println("✅ Extent Report saved successfully.");
        }
    }
}