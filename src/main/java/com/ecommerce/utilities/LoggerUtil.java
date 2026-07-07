package com.ecommerce.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * LoggerUtil - Provides a centralized way to get Logger instances.
 *
 * Why a utility class?
 * Instead of writing this in every class:
 *   private static final Logger log = LogManager.getLogger(MyClass.class);
 *
 * You just call:
 *   private static final Logger log = LoggerUtil.getLogger(MyClass.class);
 *
 * Same result but goes through our utility — future-proofs logging setup.
 */
public class LoggerUtil {

    /**
     * Returns a Logger instance for the given class.
     * Log4j uses the class name to identify WHERE the log came from.
     *
     * @param clazz - the class requesting a logger
     * @return Logger instance for that class
     */
    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }
}