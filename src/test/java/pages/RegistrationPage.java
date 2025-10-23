package pages;

import com.microsoft.playwright.Page;

/**
 * Page Object Model for the Registration Page
 * Provides methods to interact with signup/registration form elements
 */
public class RegistrationPage {
    private final Page page;
    
    /**
     * Constructs a new RegistrationPage object
     * @param page Playwright Page instance
     */
    public RegistrationPage(Page page) {
        this.page = page;
    }

    /**
     * Verifies that the "New User Signup!" text is visible
     * @return true if signup section is visible
     */
    public boolean verifyNewUserSignup() {
        try {
            return page.locator("text=New User Signup!").isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Initiates the signup process by filling name and email
     * @param name User's full name
     * @param email User's email address
     */
    public void startSignup(String name, String email) {
        page.locator("input[data-qa='signup-name']").fill(name);
        page.locator("input[data-qa='signup-email']").fill(email);
        page.locator("button[data-qa='signup-button']").click();
        page.waitForLoadState();
    }

    /**
     * Verifies that the "Enter Account Information" page is displayed
     * @return true if account information page is visible
     */
    public boolean verifyAccountInformationPage() {
        try {
            return page.locator("text=Enter Account Information").isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Fills all account details required for registration
     * @param password User's password
     */
    public void fillAccountDetails(String password) {
        page.locator("#id_gender1").check();
        page.locator("#password").fill(password);
        page.locator("#days").selectOption("10");
        page.locator("#months").selectOption("5");
        page.locator("#years").selectOption("1994");
        page.locator("#newsletter").check();
        page.locator("#optin").check();
        page.locator("#first_name").fill("Test");
        page.locator("#last_name").fill("User");
        page.locator("#company").fill("DemoCo");
        page.locator("#address1").fill("123 Test St");
        page.locator("#country").selectOption("Canada");
        page.locator("#state").fill("ON");
        page.locator("#city").fill("Toronto");
        page.locator("#zipcode").fill("A1A1A1");
        page.locator("#mobile_number").fill("+1234567890");
    }

    /**
     * Submits the account creation form
     */
    public void submitAccount() {
        page.locator("button[data-qa='create-account']").click();
        page.waitForLoadState();
    }

    /**
     * Checks if the account created confirmation message is visible
     * @return true if account was created successfully, false otherwise
     */
    public boolean isAccountCreated() {
        try {
            return page.locator("h2[data-qa='account-created']").or(page.locator("text=Account Created!")).isVisible();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Verifies if duplicate email error is shown
     * @return true if error message is visible
     */
    public boolean isDuplicateEmailError() {
        try {
            return page.locator("text=Email Address already exist!").isVisible();
        } catch (Exception e) {
            return false;
        }
    }
}
