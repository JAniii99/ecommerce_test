package com.ecommerce.utilities;

import org.apache.logging.log4j.Logger;
import org.testng.annotations.DataProvider;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * DataProviderUtil - Supplies test data from CSV files to TestNG tests.
 *
 * Why CSV over hardcoded data?
 * - Test data changes without touching test code
 * - Non-technical team members can update test data
 * - Same test logic runs with multiple data combinations
 * - Easier to add edge cases
 *
 * How TestNG DataProvider works:
 * - Method annotated with @DataProvider returns Object[][]
 * - Each Object[] is one row of test data
 * - TestNG runs the @Test method once per row automatically
 */
public class DataProviderUtil {

    private static final Logger log = LoggerUtil.getLogger(DataProviderUtil.class);

    // Path to login test data CSV file
    private static final String LOGIN_DATA_FILE =
            "src/test/resources/loginTestData.csv";

    /**
     * Provides login test data from CSV file.
     *
     * Returns Object[][] where each row contains:
     * [0] testCase    - test case ID (e.g. TC_LOGIN_01)
     * [1] email       - email address to use
     * [2] password    - password to use
     * [3] expected    - "success" or "failure"
     *
     * TestNG runs the consuming @Test method once per row.
     *
     * @return Object[][] of test data rows
     */
    @DataProvider(name = "loginData")
    public static Object[][] getLoginData() {
        List<Object[]> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new FileReader(LOGIN_DATA_FILE))) {

            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                // Skip the header row
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                // Skip empty lines
                if (line.trim().isEmpty()) continue;

                // Split by comma
                // -1 keeps trailing empty strings (for empty password)
                String[] values = line.split(",", -1);

                if (values.length >= 4) {
                    data.add(new Object[]{
                            values[0].trim(),  // testCase
                            values[1].trim(),  // email
                            values[2].trim(),  // password
                            values[3].trim()   // expectedResult
                    });
                    log.debug("Loaded test data row: {}", values[0]);
                }
            }

            log.info("Loaded {} login test data rows from CSV.", data.size());

        } catch (IOException e) {
            log.error("Failed to read login test data file: {}", e.getMessage());
            throw new RuntimeException("Cannot read test data: " + LOGIN_DATA_FILE, e);
        }

        // Convert List to Object[][] required by TestNG
        return data.toArray(new Object[0][]);
    }
}