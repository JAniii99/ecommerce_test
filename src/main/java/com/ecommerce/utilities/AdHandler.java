package com.ecommerce.utilities;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * AdHandler - Handles Google Ad overlays on automationexercise.com
 *
 * The site shows Google Ads in iframes that cover page elements.
 * This utility removes ad iframes using JavaScript so our tests
 * can interact with the actual page elements underneath.
 *
 * Why JavaScript? Because Selenium cannot directly interact with
 * cross-origin iframes. JavaScript can remove them from the DOM.
 */
public class AdHandler {

    private static final Logger log = LoggerUtil.getLogger(AdHandler.class);

    /**
     * Removes all Google Ad iframes from the page using JavaScript.
     * Call this after navigating to any page that may show ads.
     *
     * @param driver - WebDriver instance
     */
    public static void closeAds(WebDriver driver) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;

            // Remove all ad iframes by their common identifiers
            // aswift_ is Google's ad iframe naming pattern
            js.executeScript(
                    "var ads = document.querySelectorAll(" +
                            "'iframe[id^=\"aswift\"], " +
                            "iframe[title=\"Advertisement\"], " +
                            "div[id=\"ad_position_box\"], " +
                            "ins.adsbygoogle');" +
                            "ads.forEach(function(ad) { " +
                            "   if(ad && ad.parentNode) { " +
                            "       ad.parentNode.removeChild(ad); " +
                            "   }" +
                            "});"
            );

            log.debug("Ad iframes removed from page.");

        } catch (Exception e) {
            log.debug("No ads found or already removed: {}", e.getMessage());
        }
    }

    /**
     * Clicks an element using JavaScript instead of Selenium's click().
     * Use this when ads or overlays intercept normal clicks.
     *
     * Why JS click? JavaScript bypasses the visual click interception
     * and directly triggers the click event on the element.
     *
     * @param driver  - WebDriver instance
     * @param element - element to click
     */
    public static void jsClick(WebDriver driver, WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);
            log.debug("JavaScript click executed on element.");
        } catch (Exception e) {
            log.error("JavaScript click failed: {}", e.getMessage());
            throw new RuntimeException("JS click failed", e);
        }
    }
}