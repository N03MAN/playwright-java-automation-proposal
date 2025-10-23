package utils;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

/**
 * Browser Manager - Single Responsibility: Browser Lifecycle Management
 * Provides thread-safe browser instances for parallel execution
 */
public class BrowserManager {
    private static final ThreadLocal<Playwright> playwrightThread = new ThreadLocal<>();
    private static final ThreadLocal<Browser> browserThread = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> contextThread = new ThreadLocal<>();
    private static final ThreadLocal<Page> pageThread = new ThreadLocal<>();
    
    /**
     * Initialize browser with configuration
     * @return Configured Page instance
     */
    public static Page initializeBrowser() {
        // Clean up any existing instances first
        closeBrowser();
        
        // Create new instances
        Playwright playwright = Playwright.create();
        playwrightThread.set(playwright);
        
        // Get configuration from system properties
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));
        int slowMo = Integer.parseInt(System.getProperty("slowMo", headless ? "0" : "100"));
        String browserType = System.getProperty("browser", "chromium");
        
        // Launch browser based on type
        Browser browser = launchBrowser(playwright, browserType, headless, slowMo);
        browserThread.set(browser);
        
        // Create context with viewport
        BrowserContext context = browser.newContext(
            new Browser.NewContextOptions()
                .setViewportSize(1920, 1080)
                .setIgnoreHTTPSErrors(true)
        );
        contextThread.set(context);
        
        // Create page
        Page page = context.newPage();
        page.setDefaultTimeout(30000); // 30 seconds default timeout
        pageThread.set(page);
        
        return page;
    }
    
    /**
     * Launch browser based on type
     */
    private static Browser launchBrowser(Playwright playwright, String browserType, boolean headless, int slowMo) {
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
            .setHeadless(headless)
            .setTimeout(60000)
            .setSlowMo(slowMo);
            
        switch (browserType.toLowerCase()) {
            case "firefox":
                return playwright.firefox().launch(options);
            case "webkit":
                return playwright.webkit().launch(options);
            case "chromium":
            default:
                return playwright.chromium().launch(options);
        }
    }
    
    /**
     * Get current page instance
     */
    public static Page getPage() {
        return pageThread.get();
    }
    
    /**
     * Get current context instance
     */
    public static BrowserContext getContext() {
        return contextThread.get();
    }
    
    /**
     * Close browser and cleanup resources
     */
    public static void closeBrowser() {
        try {
            // Close page
            Page page = pageThread.get();
            if (page != null) {
                try {
                    if (!page.isClosed()) {
                        page.close();
                    }
                } catch (Exception e) {
                    // Ignore
                } finally {
                    pageThread.remove();
                }
            }
            
            // Close context
            BrowserContext context = contextThread.get();
            if (context != null) {
                try {
                    context.close();
                } catch (Exception e) {
                    // Ignore
                } finally {
                    contextThread.remove();
                }
            }
            
            // Close browser
            Browser browser = browserThread.get();
            if (browser != null) {
                try {
                    if (browser.isConnected()) {
                        browser.close();
                    }
                } catch (Exception e) {
                    // Ignore
                } finally {
                    browserThread.remove();
                }
            }
            
            // Close playwright
            Playwright playwright = playwrightThread.get();
            if (playwright != null) {
                try {
                    playwright.close();
                } catch (Exception e) {
                    // Ignore
                } finally {
                    playwrightThread.remove();
                }
            }
        } catch (Exception e) {
            System.err.println("Error during browser cleanup: " + e.getMessage());
        }
    }
}
