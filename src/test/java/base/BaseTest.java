package base;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

/**
 * Base test class for all test suites
 * Manages Playwright browser lifecycle
 */
public class BaseTest {
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeClass(alwaysRun = true)
    public void setUpClass() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
            new BrowserType.LaunchOptions()
                .setHeadless(true)
                .setTimeout(60000) // 60 seconds timeout
        );
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        context = browser.newContext(
            new Browser.NewContextOptions()
                .setViewportSize(1920, 1080)
        );
        page = context.newPage();
        page.setDefaultTimeout(30000); // 30 seconds default timeout
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        try {
            if (page != null && !page.isClosed()) {
                page.close();
            }
        } catch (Exception e) {
            // Ignore if already closed
        }
        
        try {
            if (context != null) {
                context.close();
            }
        } catch (Exception e) {
            // Ignore if already closed
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        try {
            if (browser != null) {
                browser.close();
            }
        } catch (Exception e) {
            // Ignore if already closed
        }
        
        try {
            if (playwright != null) {
                playwright.close();
            }
        } catch (Exception e) {
            // Ignore if already closed
        }
    }
}
