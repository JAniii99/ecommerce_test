package com.ecommerce.pages;

import com.ecommerce.utilities.AdHandler;
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
 * CheckoutPage - Page Object for the complete Checkout flow.
 *
 * Covers 4 pages:
 * Page 1: Cart page - "Proceed To Checkout" button
 * Page 2: Checkout page - Address + Order Summary + Comment
 * Page 3: Payment page - Card details form
 * Page 4: Order confirmation - "Order Placed!" success
 */
public class CheckoutPage {

    private static final Logger log = LoggerUtil.getLogger(CheckoutPage.class);

    private WebDriver driver;
    private WebDriverWait wait;

    // ==================== CART PAGE LOCATORS ====================

    // "Proceed To Checkout" button on cart page
    private By proceedToCheckoutBtn = By.xpath(
            "//a[contains(text(),'Proceed To Checkout')]"
    );

    // Login modal elements (shown when not logged in)
    private By checkoutModalTitle   = By.xpath("//h4[contains(text(),'Checkout')]");
    private By registerLoginLink    = By.xpath("//u[contains(text(),'Register / Login')]");
    private By continueOnCartBtn    = By.xpath(
            "//button[contains(@class,'close-checkout-modal')]"
    );

    // ==================== CHECKOUT PAGE LOCATORS ====================

    // Address section
    private By deliveryAddress      = By.id("address_delivery");
    private By billingAddress       = By.id("address_invoice");
    private By checkoutInfoSection  = By.xpath("//div[@data-qa='checkout-info']");

    // Order summary
    private By orderSummaryTable    = By.id("cart_info");
    private By orderProductNames    = By.xpath(
            "//div[@id='cart_info']//td[@class='cart_description']//h4/a"
    );
    private By orderTotalPrice      = By.xpath(
            "//div[@id='cart_info']//tr[last()]//p[@class='cart_total_price']"
    );

    // Comment field and Place Order
    private By commentField         = By.xpath("//textarea[@name='message']");
    private By placeOrderBtn        = By.xpath("//a[@class='btn btn-default check_out']");

    // ==================== PAYMENT PAGE LOCATORS ====================

    private By nameOnCard           = By.xpath("//input[@data-qa='name-on-card']");
    private By cardNumber           = By.xpath("//input[@data-qa='card-number']");
    private By cardCvc              = By.xpath("//input[@data-qa='cvc']");
    private By expiryMonth          = By.xpath("//input[@data-qa='expiry-month']");
    private By expiryYear           = By.xpath("//input[@data-qa='expiry-year']");
    private By payConfirmBtn        = By.xpath("//button[@data-qa='pay-button']");

    // ==================== CONFIRMATION LOCATORS ====================

    private By orderSuccessMessage  = By.xpath(
            "//div[contains(@class,'alert-success')]"
    );
    private By orderPlacedTitle     = By.xpath(
            "//b[contains(text(),'Order Placed!')]"
    );

    // ==================== CONSTRUCTOR ====================

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(ConfigReader.getInt("explicit.wait"))
        );
        log.debug("CheckoutPage initialized.");
    }

    // ==================== CART PAGE ACTIONS ====================

    /**
     * Clicks "Proceed To Checkout" button on the cart page.
     * If user is not logged in, a modal appears.
     * If user is logged in, goes directly to checkout page.
     */
    public void clickProceedToCheckout() {
        log.info("Clicking Proceed To Checkout button.");
        AdHandler.closeAds(driver);
        WebElement btn = wait.until(
                ExpectedConditions.elementToBeClickable(proceedToCheckoutBtn)
        );
        AdHandler.jsClick(driver, btn);
        log.info("Clicked Proceed To Checkout.");
    }

    /**
     * Checks if the "Register/Login" modal appeared.
     * This happens when user is NOT logged in.
     *
     * @return true if modal is visible
     */
    public boolean isLoginModalVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(checkoutModalTitle));
            log.info("Checkout login modal is visible.");
            return true;
        } catch (Exception e) {
            log.info("No login modal - user is already logged in.");
            return false;
        }
    }

    /**
     * Clicks "Register/Login" link in the checkout modal.
     * Redirects to login page.
     */
    public void clickRegisterLoginInModal() {
        log.info("Clicking Register/Login link in checkout modal.");
        wait.until(ExpectedConditions.elementToBeClickable(registerLoginLink)).click();
    }

    // ==================== CHECKOUT PAGE ACTIONS ====================

    /**
     * Verifies we are on the checkout page by checking
     * the address section is visible.
     *
     * @return true if checkout page loaded successfully
     */
    public boolean isOnCheckoutPage() {
        try {
            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(checkoutInfoSection)
            );
            log.info("Successfully on Checkout page.");
            return true;
        } catch (Exception e) {
            log.warn("Not on Checkout page.");
            return false;
        }
    }

    /**
     * Checks if delivery address section is displayed.
     *
     * @return true if address is shown
     */
    public boolean isDeliveryAddressDisplayed() {
        try {
            WebElement addr = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(deliveryAddress)
            );
            String text = addr.getText();
            log.info("Delivery address displayed: {}", text.substring(0, 20) + "...");
            return !text.isEmpty();
        } catch (Exception e) {
            log.warn("Delivery address NOT displayed.");
            return false;
        }
    }

    /**
     * Gets list of product names in the order summary.
     *
     * @return List of product name strings
     */
    public List<String> getOrderSummaryProducts() {
        List<WebElement> elements = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(orderProductNames)
        );
        List<String> names = elements.stream()
                .map(e -> e.getText().trim())
                .collect(java.util.stream.Collectors.toList());
        log.info("Order summary products: {}", names);
        return names;
    }

    /**
     * Gets the total price shown in order summary.
     *
     * @return total price string e.g. "Rs. 500"
     */
    public String getOrderTotalPrice() {
        String total = wait.until(
                ExpectedConditions.visibilityOfElementLocated(orderTotalPrice)
        ).getText().trim();
        log.info("Order total price: {}", total);
        return total;
    }

    /**
     * Adds a comment in the order comment field.
     *
     * @param comment - text to enter in comment box
     */
    public void addOrderComment(String comment) {
        log.info("Adding order comment: {}", comment);
        WebElement field = wait.until(
                ExpectedConditions.visibilityOfElementLocated(commentField)
        );
        field.clear();
        field.sendKeys(comment);
    }

    /**
     * Clicks "Place Order" button to proceed to payment.
     */
    public void clickPlaceOrder() {
        log.info("Clicking Place Order button.");
        wait.until(ExpectedConditions.elementToBeClickable(placeOrderBtn)).click();
        log.info("Navigated to Payment page.");
    }

    // ==================== PAYMENT PAGE ACTIONS ====================

    /**
     * Fills in the payment card details form.
     *
     * @param name  - name on card
     * @param num   - card number
     * @param cvc   - security code
     * @param month - expiry month MM
     * @param year  - expiry year YYYY
     */
    public void fillPaymentDetails(String name, String num,
                                   String cvc, String month, String year) {
        log.info("Filling payment details.");

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(nameOnCard)
        ).sendKeys(name);

        driver.findElement(cardNumber).sendKeys(num);
        driver.findElement(cardCvc).sendKeys(cvc);
        driver.findElement(expiryMonth).sendKeys(month);
        driver.findElement(expiryYear).sendKeys(year);

        log.info("Payment details filled successfully.");
    }

    /**
     * Clicks "Pay and Confirm Order" button.
     */
    public void clickPayAndConfirm() {
        log.info("Clicking Pay and Confirm Order button.");
        wait.until(
                ExpectedConditions.elementToBeClickable(payConfirmBtn)
        ).click();
        log.info("Payment submitted.");
    }

    // ==================== CONFIRMATION ====================

    /**
     * Checks if order was placed successfully.
     * Looks for "Order Placed!" title OR success alert message.
     *
     * @return true if order confirmed
     */
    public boolean isOrderPlacedSuccessfully() {
        try {
            // Try Order Placed! title first
            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(orderPlacedTitle)
            );
            log.info("Order Placed! confirmation found.");
            return true;
        } catch (Exception e) {
            try {
                // Fall back to success alert message
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(orderSuccessMessage)
                );
                log.info("Order success alert found.");
                return true;
            } catch (Exception ex) {
                log.warn("Order confirmation NOT found.");
                return false;
            }
        }
    }
}