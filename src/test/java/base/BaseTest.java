package base;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.microsoft.playwright.Page;

import io.qameta.allure.Attachment;
import utils.BrowserManager;

/**
 * Base Test Class - Single Responsibility: Test Lifecycle Management
 * Manages browser initialization and cleanup for each test method
 * Uses BrowserManager for browser operations (SRP)
 */
public class BaseTest {
    protected Page page;
    
    /**
     * Setup method - runs before EACH test method
     * Creates a fresh browser instance for complete test isolation
     */
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        // Initialize browser for this test
        page = BrowserManager.initializeBrowser();
    }

    /**
     * Teardown method - runs after EACH test method
     * Captures screenshot on failure and closes browser
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown(org.testng.ITestResult result) {
        try {
            // Capture screenshot if test failed
            if (!result.isSuccess() && page != null && !page.isClosed()) {
                captureScreenshot("Failed_" + result.getMethod().getMethodName());
            }
        } catch (Exception e) {
            // Ignore screenshot capture errors
        } finally {
            // Always close browser to prevent leaks
            BrowserManager.closeBrowser();
        }
    }
    
    /**
     * Capture and attach screenshot to Allure report
     * @param name Screenshot name
     * @return Screenshot as byte array
     */
    @Attachment(value = "{name}", type = "image/png")
    protected byte[] captureScreenshot(String name) {
        try {
            if (page != null && !page.isClosed()) {
                return page.screenshot();
            }
        } catch (Exception e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
        }
        return new byte[0];
    }
    
    /**
     * Get current page instance
     * @return Current Page object
     */
    protected Page getPage() {
        if (page == null || page.isClosed()) {
            page = BrowserManager.initializeBrowser();
        }
        return page;
    }
}
