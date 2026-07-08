package com.ecommerce.pages;

import com.ecommerce.utilities.ConfigReader;
import com.ecommerce.utilities.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * ProductSearchPage - Page Object for Product Search feature.
 *
 * Covers:
 * - Navigating to Products page
 * - Searching for a product
 * - Reading search results
 * - Verifying results contain searched product
 */
public class ProductSearchPage {

    private static final Logger log = LoggerUtil.getLogger(ProductSearchPage.class);

    private WebDriver driver;
    private WebDriverWait wait;

    // ==================== LOCATORS ====================

    // Navigation
    private By productsNavLink = By.xpath("//a[@href='/products']");

    // Search elements
    private By searchInput     = By.id("search_product");
    private By searchButton    = By.id("submit_search");

    // Results
    // This finds ALL product name <p> tags inside .productinfo divs
    private By productNames    = By.xpath("//div[@class='productinfo text-center']/p");

    // "No products found" indicator — when search returns nothing
    // We check if product list is empty instead
    private By productCards    = By.xpath("//div[@class='product-image-wrapper']");

    // Search results title shown after search
    private By searchedProductsTitle = By.xpath(
            "//h2[contains(text(),'Searched Products')]"
    );

    // ==================== CONSTRUCTOR ====================

    public ProductSearchPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(ConfigReader.getInt("explicit.wait"))
        );
        log.debug("ProductSearchPage initialized.");
    }

    // ==================== ACTIONS ====================

    /**
     * Clicks the "Products" link in the navigation bar.
     * This takes us to the All Products page where search is available.
     */
    public void navigateToProductsPage() {
        log.info("Navigating to Products page.");
        wait.until(ExpectedConditions.elementToBeClickable(productsNavLink)).click();

        // Wait for search input to confirm we're on the right page
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        log.info("Successfully landed on Products page.");
    }

    /**
     * Types a search term into the search box and clicks Search.
     *
     * @param searchTerm - the product name to search for
     */
    public void searchForProduct(String searchTerm) {
        log.info("Searching for product: '{}'", searchTerm);

        // Clear and type in search box
        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(searchInput)
        );
        input.clear();
        input.sendKeys(searchTerm);

        // Click search button
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
        log.info("Search submitted for: '{}'", searchTerm);
    }

    /**
     * Checks if "Searched Products" title appears after search.
     * This confirms the search was executed successfully.
     *
     * @return true if search results section is visible
     */
    public boolean isSearchResultsTitleVisible() {
        try {
            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(searchedProductsTitle)
            );
            log.info("'Searched Products' title is visible.");
            return true;
        } catch (Exception e) {
            log.warn("'Searched Products' title NOT visible.");
            return false;
        }
    }

    /**
     * Gets the count of product cards shown in search results.
     * Returns 0 if no products found.
     *
     * @return number of products in results
     */
    public int getSearchResultsCount() {
        try {
            // Wait briefly for results to load
            wait.until(ExpectedConditions.visibilityOfElementLocated(productCards));
            List<WebElement> cards = driver.findElements(productCards);
            log.info("Search returned {} product(s).", cards.size());
            return cards.size();
        } catch (Exception e) {
            log.warn("No product cards found in results.");
            return 0;
        }
    }

    /**
     * Checks if ANY result contains the searched product name.
     * Case-insensitive comparison for reliability.
     *
     * Example: searching "blue top" matches "Blue Top"
     *
     * @param productName - the product name to look for in results
     * @return true if at least one result matches
     */
    public boolean isProductFoundInResults(String productName) {
        try {
            // Get all product name elements
            List<WebElement> names = wait.until(
                    ExpectedConditions.visibilityOfAllElementsLocatedBy(productNames)
            );

            // Loop through each product name and check for match
            for (WebElement nameElement : names) {
                String text = nameElement.getText().trim();
                log.debug("Found product in results: '{}'", text);

                // Case-insensitive contains check
                if (text.toLowerCase().contains(productName.toLowerCase())) {
                    log.info("✅ Product '{}' found in search results!", productName);
                    return true;
                }
            }

            log.warn("Product '{}' NOT found in search results.", productName);
            return false;

        } catch (Exception e) {
            log.warn("Could not read product names from results: {}", e.getMessage());
            return false;
        }
    }
}