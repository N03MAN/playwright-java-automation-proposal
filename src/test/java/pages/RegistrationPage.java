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
     * Fills all account details from dataset (matches TypeScript implementation)
     * @param details Map containing all registration details
     */
    @SuppressWarnings("unchecked")
    public void fillAccountDetailsFromData(java.util.Map<String, Object> details) {
        // Title (Gender)
        page.locator("input[id='id_gender1']").check();
        
        // Name and Password
        page.locator("input[name='name']").fill((String) details.get("name"));
        page.locator("input[name='password']").fill((String) details.get("password"));
        
        // Date of Birth
        java.util.Map<String, Object> dob = (java.util.Map<String, Object>) details.get("dob");
        page.locator("#days").selectOption((String) dob.get("day"));
        page.locator("#months").selectOption((String) dob.get("month"));
        page.locator("#years").selectOption((String) dob.get("year"));
        
        // Checkboxes
        page.locator("input[name='newsletter']").check();
        page.locator("input[name='optin']").check();
        
        // Additional details
        page.locator("input[name='first_name']").fill((String) details.get("firstName"));
        page.locator("input[name='last_name']").fill((String) details.get("lastName"));
        page.locator("input[name='company']").fill((String) details.get("company"));
        page.locator("input[name='address1']").fill((String) details.get("address"));
        page.locator("input[name='address2']").fill((String) details.get("address2"));
        page.locator("#country").selectOption((String) details.get("country"));
        page.locator("input[name='state']").fill((String) details.get("state"));
        page.locator("input[name='city']").fill((String) details.get("city"));
        page.locator("input[name='zipcode']").fill((String) details.get("zipcode"));
        page.locator("input[name='mobile_number']").fill((String) details.get("mobileNumber"));
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
            // Multiple ways to detect account creation success
            // 1. Check URL - most reliable
            if (page.url().contains("/account_created")) {
                return true;
            }
            
            // 2. Check for success heading (various formats the site might use)
            return page.locator("h2[data-qa='account-created']").isVisible() ||
                   page.locator("text=ACCOUNT CREATED!").isVisible() ||
                   page.locator("text=Account Created!").isVisible() ||
                   page.locator("h2:has-text('Account Created')").isVisible() ||
                   page.locator("b:has-text('ACCOUNT CREATED')").isVisible();
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
