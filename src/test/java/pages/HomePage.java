package pages;

import com.microsoft.playwright.Page;

/**
 * Page Object Model for the Home Page
 * Provides methods to interact with the home page elements
 */
public class HomePage {
    private final Page page;

    /**
     * Constructs a new HomePage object
     * @param page Playwright Page instance
     */
    public HomePage(Page page) {
        this.page = page;
    }

    /**
     * Navigates to the specified URL
     * @param baseUrl The URL to navigate to
     */
    public void navigate(String baseUrl) {
        page.navigate(baseUrl);
        page.waitForLoadState();
    }

    /**
     * Verifies that the home page is displayed correctly
     * @return true if home page is visible, false otherwise
     */
    public boolean verifyHomePage() {
        return page.locator("a[href='/']").first().isVisible();
    }

    /**
     * Clicks on the 'Signup / Login' link to navigate to registration/login page
     */
    public void goToSignupLogin() {
        page.locator("a[href='/login']").click();
        page.waitForLoadState();
    }
    
    /**
     * Verifies user is logged in by checking for logout button
     * @param username Expected username
     * @return true if user is logged in with correct username
     */
    public boolean verifyLoggedIn(String username) {
        try {
            String loggedInText = page.locator("a:has-text('Logged in as')").textContent();
            return loggedInText != null && loggedInText.contains(username);
        } catch (Exception e) {
            return false;
        }
    }
}
