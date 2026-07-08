package com.ecommerce.utilities;

import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * RetryAnalyzer - Automatically retries failed tests.
 *
 * Why retry?
 * In real-world automation, tests fail for reasons unrelated
 * to actual bugs — network timeouts, ad overlays, page load
 * delays. Retrying once or twice filters out these false failures
 * so only genuine bugs are reported.
 *
 * Industry standard: retry max 2 times.
 * If test fails 3 times in a row → real failure, report it.
 *
 * Implements IRetryAnalyzer — TestNG's built-in retry interface.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger log = LoggerUtil.getLogger(RetryAnalyzer.class);

    // Current retry attempt counter (per test instance)
    private int retryCount = 0;

    // Maximum number of retries allowed
    // Total attempts = MAX_RETRY + 1 (original + retries)
    private static final int MAX_RETRY = 2;

    /**
     * TestNG calls this method every time a test FAILS.
     * Return true  → retry the test
     * Return false → mark as failed, move on
     *
     * @param result - contains info about the failed test
     * @return true if should retry, false if should fail
     */
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY) {
            retryCount++;
            log.warn(
                    "⚠️ Test '{}' FAILED. Retry attempt {}/{}",
                    result.getMethod().getMethodName(),
                    retryCount,
                    MAX_RETRY
            );
            return true;   // Tell TestNG to run the test again
        }

        log.error(
                "❌ Test '{}' failed after {} retries. Marking as FAILED.",
                result.getMethod().getMethodName(),
                MAX_RETRY
        );
        return false;  // Tell TestNG to stop retrying
    }
}