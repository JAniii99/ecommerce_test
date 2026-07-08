package com.ecommerce.pages;

import com.ecommerce.utilities.ConfigReader;
import com.ecommerce.utilities.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.ecommerce.utilities.AdHandler;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;
import java.util.List;

/**
 * CartPage - Page Object for Add to Cart and Cart page.
 *
 * Covers:
 * - Adding products to cart from Products page
 * - Handling the "Added!" modal popup
 * - Navigating to cart
 * - Verifying cart contents
 * - Verifying cart quantity
 */
public class CartPage {

    private static final Logger log = LoggerUtil.getLogger(CartPage.class);

    private WebDriver driver;
    private WebDriverWait wait;

    // ==================== LOCATORS ====================

    // Navigation
    private By productsNavLink  = By.xpath("//a[@href='/products']");
    private By cartNavLink      = By.xpath("//a[@href='/view_cart']");

    // Product listing page
    // Gets ALL "Add to cart" buttons on the page
    private By addToCartButtons = By.xpath(
            "//div[@class='productinfo text-center']//a[contains(@class,'add-to-cart')]"
    );

    // Modal popup elements
    private By modalTitle       = By.xpath("//h4[@class='modal-title w-100']");
    private By viewCartLink     = By.xpath("//u[contains(text(),'View Cart')]");
    private By continueShoppingBtn = By.xpath(
            "//button[contains(@class,'close-modal')]"
    );

    // Cart page elements
    private By cartTable        = By.id("cart_info_table");
    private By cartProductNames = By.xpath(
            "//td[@class='cart_description']//h4/a"
    );
    private By cartQuantities   = By.xpath(
            "//td[@class='cart_quantity']//button"
    );
    private By cartRows         = By.xpath("//tr[contains(@id,'product-')]");
    private By emptyCartMessage = By.id("empty_cart");

    // ==================== CONSTRUCTOR ====================

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(ConfigReader.getInt("explicit.wait"))
        );
        log.debug("CartPage initialized.");
    }

    // ==================== NAVIGATION ====================

    /**
     * Navigates to the Products page.
     */
    public void navigateToProductsPage() {
        log.info("Navigating to Products page.");
        wait.until(ExpectedConditions.elementToBeClickable(productsNavLink)).click();

        // Wait for page to load
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@class='productinfo text-center']")
        ));

        // Remove ads that may block clicks
        AdHandler.closeAds(driver);
        log.info("Successfully on Products page - ads removed.");
    }

    /**
     * Navigates directly to the Cart page via nav link.
     */
    public void navigateToCartPage() {
        log.info("Navigating to Cart page.");
        wait.until(ExpectedConditions.elementToBeClickable(cartNavLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(cartTable));
        log.info("Successfully on Cart page.");
    }

    // ==================== ADD TO CART ACTIONS ====================

    /**
     * Adds the FIRST product on the Products page to cart.
     * Hovers to reveal the Add to Cart button then clicks it.
     */
    public void addFirstProductToCart() {
        log.info("Adding first product to cart.");

        // Remove ads before finding buttons
        AdHandler.closeAds(driver);

        List<WebElement> buttons = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(addToCartButtons)
        );

        // Use JavaScript click to bypass any remaining overlay
        AdHandler.jsClick(driver, buttons.get(0));
        log.info("Clicked Add to Cart for first product.");
        waitForModal();
    }

    /**
     * Adds a SPECIFIC product by index (0-based).
     * Index 0 = first product, 1 = second product, etc.
     *
     * @param index - 0-based index of the product to add
     */
    public void addProductToCartByIndex(int index) {
        log.info("Adding product at index {} to cart.", index);

        // Remove ads before interaction
        AdHandler.closeAds(driver);

        List<WebElement> buttons = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(addToCartButtons)
        );

        if (index >= buttons.size()) {
            throw new RuntimeException(
                    "Product index " + index + " out of range. " +
                            "Only " + buttons.size() + " products available."
            );
        }

        // Use JavaScript click to bypass ad overlays
        AdHandler.jsClick(driver, buttons.get(index));
        log.info("JS clicked Add to Cart for product at index {}.", index);
        waitForModal();
    }

    // ==================== MODAL ACTIONS ====================

    /**
     * Waits for the "Added!" modal to appear.
     * Called automatically after adding to cart.
     */
    private void waitForModal() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(modalTitle));
        log.info("'Added!' modal appeared.");
    }

    /**
     * Checks if the "Added!" modal is visible.
     * Used to verify product was added successfully.
     *
     * @return true if modal is visible
     */
    public boolean isAddedToCartModalVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(modalTitle));
            String title = driver.findElement(modalTitle).getText();
            log.info("Modal visible with title: '{}'", title);
            return title.contains("Added");
        } catch (Exception e) {
            log.warn("Added! modal not visible.");
            return false;
        }
    }

    /**
     * Clicks "Continue Shopping" in the modal.
     * Closes modal and stays on Products page.
     */
    public void clickContinueShopping() {
        log.info("Clicking Continue Shopping.");
        wait.until(
                ExpectedConditions.elementToBeClickable(continueShoppingBtn)
        ).click();
        log.info("Continued shopping - modal closed.");
    }

    /**
     * Clicks "View Cart" link in the modal.
     * Takes us to the Cart page.
     */
    public void clickViewCart() {
        log.info("Clicking View Cart link.");
        wait.until(ExpectedConditions.elementToBeClickable(viewCartLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(cartTable));
        log.info("Navigated to Cart page via modal.");
    }

    // ==================== CART VERIFICATION ====================

    /**
     * Gets count of unique products in the cart.
     * Each row in the cart table = one product.
     *
     * @return number of product rows in cart
     */
    public int getCartItemCount() {
        try {
            List<WebElement> rows = wait.until(
                    ExpectedConditions.visibilityOfAllElementsLocatedBy(cartRows)
            );
            log.info("Cart contains {} item(s).", rows.size());
            return rows.size();
        } catch (Exception e) {
            log.warn("No items found in cart.");
            return 0;
        }
    }

    /**
     * Checks if a specific product exists in the cart by name.
     *
     * @param productName - name to look for in cart
     * @return true if product found in cart
     */
    public boolean isProductInCart(String productName) {
        try {
            List<WebElement> names = wait.until(
                    ExpectedConditions.visibilityOfAllElementsLocatedBy(cartProductNames)
            );

            for (WebElement name : names) {
                String text = name.getText().trim();
                log.debug("Cart product: '{}'", text);
                if (text.equalsIgnoreCase(productName)) {
                    log.info("Product '{}' found in cart.", productName);
                    return true;
                }
            }

            log.warn("Product '{}' NOT found in cart.", productName);
            return false;

        } catch (Exception e) {
            log.warn("Could not read cart products: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Gets the quantity of a specific product in the cart.
     * Returns the quantity shown in the cart quantity button.
     *
     * @param productIndex - 0-based index of the cart row
     * @return quantity as integer
     */
    public int getProductQuantityInCart(int productIndex) {
        try {
            List<WebElement> quantities = wait.until(
                    ExpectedConditions.visibilityOfAllElementsLocatedBy(cartQuantities)
            );
            String qty = quantities.get(productIndex).getText().trim();
            int quantity = Integer.parseInt(qty);
            log.info("Product at index {} has quantity: {}", productIndex, quantity);
            return quantity;
        } catch (Exception e) {
            log.warn("Could not read quantity: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * Gets all product names currently in the cart.
     *
     * @return List of product name strings
     */
    public List<String> getAllCartProductNames() {
        List<WebElement> nameElements = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(cartProductNames)
        );

        List<String> names = nameElements.stream()
                .map(e -> e.getText().trim())
                .collect(java.util.stream.Collectors.toList());

        log.info("Cart products: {}", names);
        return names;
    }
}