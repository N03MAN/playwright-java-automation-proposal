package pages;

import com.microsoft.playwright.Page;

/**
 * Page Object Model for the Login Page
 * Provides methods to interact with login form elements and validate login results
 */
public class LoginPage {
    private final Page page;
    
    /**
     * Constructs a new LoginPage object
     * @param page Playwright Page instance
     */
    public LoginPage(Page page) {
        this.page = page;
    }

    /**
     * Verifies that the login section is visible
     * @return true if "Login to your account" text is visible
     */
    public boolean verifyLoginPage() {
        try {
            return page.locator("text=Login to your account").isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Fills the email field in the login form
     * @param email Email address to enter
     */
    public void fillEmail(String email) { 
        page.locator("input[data-qa='login-email']").fill(email); 
    }
    
    /**
     * Fills the password field in the login form
     * @param password Password to enter
     */
    public void fillPassword(String password) { 
        page.locator("input[data-qa='login-password']").fill(password); 
    }
    
    /**
     * Clicks the login button to submit the form
     */
    public void submitLogin() { 
        page.locator("button[data-qa='login-button']").click();
        page.waitForLoadState();
    }
    
    /**
     * Performs complete login with email and password
     * @param email Email address
     * @param password Password
     */
    public void login(String email, String password) {
        fillEmail(email);
        fillPassword(password);
        submitLogin();
    }

    /**
     * Checks if the user is logged in by verifying the Logout link is visible
     * @return true if logged in, false otherwise
     */
    public boolean isLoggedIn() {
        try {
            return page.locator("a:has-text('Logout')").first().isVisible();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Verifies login was successful by checking for username
     * @param username Expected username
     * @return true if logged in as expected user
     */
    public boolean verifyLoginSuccess(String username) {
        try {
            // Use text locator to find "Logged in as {username}" - matches TypeScript implementation
            String expectedText = "Logged in as " + username;
            String loggedInText = page.locator("text=" + expectedText).textContent();
            return loggedInText != null && loggedInText.contains(expectedText);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifies login failure by checking for error message
     * @return true if error message is displayed
     */
    public boolean verifyLoginFailure() {
        try {
            // Matches TypeScript: "text=Your email or password is"
            return page.locator("text=Your email or password is").isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the error message text displayed for incorrect credentials
     * @return Error message text
     */
    public String errorText() {
        try {
            return page.locator("p:has-text('Your email or password is incorrect!')").textContent();
        } catch (Exception e) {
            return "Error message not found";
        }
    }
}
