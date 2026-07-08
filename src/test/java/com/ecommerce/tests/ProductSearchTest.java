package com.ecommerce.tests;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.ProductSearchPage;
import com.ecommerce.utilities.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * ProductSearchTest - Test cases for Product Search feature.
 *
 * TC006 - Search for existing product
 * TC007 - Search for non-existing product
 */
public class ProductSearchTest extends BaseTest {

    private ProductSearchPage productSearchPage;

    @BeforeMethod
    public void setUpProductSearchPage() {
        productSearchPage = new ProductSearchPage(driver);
    }

    /**
     * TC006 - Search Existing Product
     *
     * Steps:
     * 1. Navigate to Products page
     * 2. Search for "Blue Top"
     * 3. Verify "Searched Products" title appears
     * 4. Verify at least 1 result is returned
     * 5. Verify result contains "Blue Top"
     *
     * Expected: Product found in results
     */
    @Test(priority = 1, description = "TC006 - Verify search returns results for existing product")
    public void testSearchExistingProduct() {
        System.out.println("🧪 Running TC006 - Search Existing Product");

        String productName = ConfigReader.get("search.existing.product");

        // Step 1: Go to Products page
        productSearchPage.navigateToProductsPage();

        // Step 2: Search for product
        productSearchPage.searchForProduct(productName);

        // Step 3: Verify search results title appears
        Assert.assertTrue(
                productSearchPage.isSearchResultsTitleVisible(),
                "❌ 'Searched Products' title not visible after search!"
        );

        // Step 4: Verify at least 1 result returned
        int resultCount = productSearchPage.getSearchResultsCount();
        Assert.assertTrue(
                resultCount > 0,
                "❌ No products returned for search: " + productName
        );

        // Step 5: Verify correct product appears in results
        Assert.assertTrue(
                productSearchPage.isProductFoundInResults(productName),
                "❌ Product '" + productName + "' not found in search results!"
        );

        System.out.println("✅ TC006 PASSED - Product '" + productName
                + "' found! Results count: " + resultCount);
    }

    /**
     * TC007 - Search Non-Existing Product
     *
     * Steps:
     * 1. Navigate to Products page
     * 2. Search for "XYZ123NonExistentProduct"
     * 3. Verify "Searched Products" title appears
     * 4. Verify ZERO results returned
     *
     * Expected: No products found — empty results
     */
    @Test(priority = 2, description = "TC007 - Verify no results for non-existing product search")
    public void testSearchNonExistingProduct() {
        System.out.println("🧪 Running TC007 - Search Non-Existing Product");

        String productName = ConfigReader.get("search.nonexisting.product");

        // Step 1: Go to Products page
        productSearchPage.navigateToProductsPage();

        // Step 2: Search for non-existing product
        productSearchPage.searchForProduct(productName);

        // Step 3: Verify search results title appears (search ran successfully)
        Assert.assertTrue(
                productSearchPage.isSearchResultsTitleVisible(),
                "❌ 'Searched Products' title not visible after search!"
        );

        // Step 4: Verify ZERO results returned
        int resultCount = productSearchPage.getSearchResultsCount();
        Assert.assertEquals(
                resultCount,
                0,
                "❌ Expected 0 results for non-existing product but found: " + resultCount
        );

        System.out.println("✅ TC007 PASSED - No results for non-existing product. ✓");
    }
}