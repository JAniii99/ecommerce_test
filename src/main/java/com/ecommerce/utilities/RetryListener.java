package com.ecommerce.utilities;

import org.apache.logging.log4j.Logger;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * RetryListener - Automatically applies RetryAnalyzer to all tests.
 *
 * Without this: you'd need to add retryAnalyzer=RetryAnalyzer.class
 * to EVERY @Test annotation — tedious and error-prone.
 *
 * With this: add it to testng.xml ONCE → all tests get retry.
 *
 * Implements IAnnotationTransformer — TestNG processes annotations
 * before running tests, giving us a chance to modify them.
 */
public class RetryListener implements IAnnotationTransformer {

    private static final Logger log = LoggerUtil.getLogger(RetryListener.class);

    /**
     * Called by TestNG for EVERY @Test annotation before execution.
     * We use it to inject RetryAnalyzer into every test method.
     *
     * @param annotation  - the @Test annotation being processed
     * @param testClass   - the test class
     * @param testConstructor - constructor (null for methods)
     * @param testMethod  - the actual test method
     */
    @Override
    public void transform(ITestAnnotation annotation,
                          Class testClass,
                          Constructor testConstructor,
                          Method testMethod) {

        // Set RetryAnalyzer on every test annotation automatically
        annotation.setRetryAnalyzer(RetryAnalyzer.class);

        log.debug(
                "RetryAnalyzer applied to: {}",
                testMethod != null ? testMethod.getName() : "unknown"
        );
    }
}