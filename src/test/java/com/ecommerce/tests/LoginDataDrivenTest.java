package com.ecommerce.tests;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.LoginPage;
import com.ecommerce.utilities.DataProviderUtil;
import com.ecommerce.utilities.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * LoginDataDrivenTest - Data-driven login tests using CSV test data.
 *
 * Uses @DataProvider to run the SAME test logic with MULTIPLE
 * data sets automatically. TestNG runs testLoginWithData() once
 * per row in loginTestData.csv.
 *
 * This demonstrates:
 * - Data-driven testing with TestNG DataProvider
 * - Separation of test logic from test data
 * - Dynamic test execution based on external data
 */
public class LoginDataDrivenTest extends BaseTest {

    private static final Logger log = LoggerUtil.getLogger(LoginDataDrivenTest.class);
    private LoginPage loginPage;

    @BeforeMethod
    public void setUpLoginPage() {
        loginPage = new LoginPage(driver);
    }

    /**
     * Data-driven login test.
     *
     * Runs ONCE PER ROW in loginTestData.csv.
     * TestNG automatically passes each row's data as parameters.
     *
     * dataProvider = "loginData" → connects to DataProviderUtil.getLoginData()
     * dataProviderClass = DataProviderUtil.class → tells TestNG where to find it
     *
     * @param testCase       - test case ID from CSV
     * @param email          - email address from CSV
     * @param password       - password from CSV
     * @param expectedResult - "success" or "failure" from CSV
     */
    @Test(
            dataProvider = "loginData",
            dataProviderClass = DataProviderUtil.class,
            description = "Data-driven login test with multiple credential combinations"
    )
    public void testLoginWithData(String testCase, String email,
                                  String password, String expectedResult) {

        log.info("Running {} - Email: '{}', Expected: {}",
                testCase, email, expectedResult);
        System.out.println("🧪 Running " + testCase
                + " | Email: " + email
                + " | Expected: " + expectedResult);

        // Navigate to login page and attempt login
        loginPage.loginWith(email, password);

        // Assert based on expected result from CSV
        if (expectedResult.trim().equals("success")) {

            Assert.assertTrue(
                    loginPage.isLoginSuccessful(),
                    testCase + " ❌ Expected successful login but it failed!"
            );
            System.out.println("✅ " + testCase + " PASSED - Login successful!");

        } else {

            Assert.assertFalse(
                    loginPage.isLoginSuccessful(),
                    testCase + " ❌ Expected login to fail but it succeeded!"
            );
            System.out.println("✅ " + testCase + " PASSED - Login correctly failed!");
        }
    }
}