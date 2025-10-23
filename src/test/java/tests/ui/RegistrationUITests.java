package tests.ui;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import pages.HomePage;
import pages.RegistrationPage;
import utils.ConfigManager;
import utils.DataUtils;
import utils.TestListeners;

/**
 * UI test suite for User Registration functionality
 * Tests cover positive scenarios, negative scenarios, and edge cases
 */
@Listeners({TestListeners.class})
@Epic("UI")
@Feature("Registration")
public class RegistrationUITests extends BaseTest {

    @Test(description = "User can register successfully via UI (happy path)", priority = 1)
    @Story("Positive Registration Scenarios")
    @Description("Creates a new account using AutomationExercise.com and verifies success banner.")
    public void testRegistrationHappyPath() {
        String baseUrl = ConfigManager.getBaseUrl();
        HomePage home = new HomePage(page);
        
        // Step 1: Navigate and verify home page
        home.navigate(baseUrl);
        Assert.assertTrue(home.verifyHomePage(), 
            "Home page should be displayed");
        
        // Step 2: Go to signup/login page
        home.goToSignupLogin();
        TestListeners.currentPage.set(page);
        
        // Step 3: Verify signup section
        RegistrationPage reg = new RegistrationPage(page);
        Assert.assertTrue(reg.verifyNewUserSignup(), 
            "New User Signup section should be visible");
        
        // Step 4: Enter name and email
        String email = DataUtils.uniqueEmail();
        String name = DataUtils.randomName();
        reg.startSignup(name, email);
        
        // Step 5: Verify account information page
        Assert.assertTrue(reg.verifyAccountInformationPage(), 
            "Enter Account Information page should be displayed");
        
        // Step 6: Fill account details
        reg.fillAccountDetails("Passw0rd!");
        
        // Step 7: Submit and verify account creation
        reg.submitAccount();
        Assert.assertTrue(reg.isAccountCreated(), 
            "Expected 'Account Created!' confirmation message to be displayed");
    }

    @Test(description = "Registration with existing email shows error", priority = 2)
    @Story("Negative Registration Scenarios")
    @Description("Verifies that duplicate email registration is prevented")
    public void testRegistrationWithDuplicateEmail() {
        String baseUrl = ConfigManager.getBaseUrl();
        String email = DataUtils.uniqueEmail();
        String name = DataUtils.randomName();
        
        // First registration
        HomePage home1 = new HomePage(page);
        home1.navigate(baseUrl);
        home1.goToSignupLogin();
        
        RegistrationPage reg1 = new RegistrationPage(page);
        reg1.startSignup(name, email);
        reg1.fillAccountDetails("Passw0rd!");
        reg1.submitAccount();
        
        Assert.assertTrue(reg1.isAccountCreated(), 
            "First registration should succeed");
        
        // Navigate to registration again
        page.navigate(baseUrl);
        HomePage home2 = new HomePage(page);
        home2.goToSignupLogin();
        
        TestListeners.currentPage.set(page);
        
        // Attempt second registration with same email
        RegistrationPage reg2 = new RegistrationPage(page);
        reg2.startSignup(name, email);
        
        // Verify error message or that we stay on the signup page
        String pageContent = page.content().toLowerCase();
        Assert.assertTrue(
            pageContent.contains("already exist") || 
            pageContent.contains("email address already"),
            "Expected error message for duplicate email"
        );
    }

    @Test(description = "Registration with empty fields", priority = 3)
    @Story("Negative Registration Scenarios")
    @Description("Verifies validation for required fields")
    public void testRegistrationWithEmptyFields() {
        String baseUrl = ConfigManager.getBaseUrl();
        HomePage home = new HomePage(page);
        home.navigate(baseUrl);
        home.goToSignupLogin();
        
        TestListeners.currentPage.set(page);
        
        // Try to click signup button without filling fields
        page.locator("button[data-qa='signup-button']").click();
        
        // Verify we're still on the signup page (validation should prevent submission)
        Assert.assertTrue(
            page.url().contains("signup") || page.url().contains("login"),
            "Should remain on signup page when required fields are empty"
        );
    }

    @Test(description = "Registration with special characters in name", priority = 4)
    @Story("Edge Cases")
    @Description("Verifies registration handles special characters in name field")
    public void testRegistrationWithSpecialCharacters() {
        String baseUrl = ConfigManager.getBaseUrl();
        HomePage home = new HomePage(page);
        home.navigate(baseUrl);
        home.goToSignupLogin();
        
        TestListeners.currentPage.set(page);
        
        String specialName = "Test!@#$%User";
        String email = DataUtils.uniqueEmail();
        
        RegistrationPage reg = new RegistrationPage(page);
        reg.startSignup(specialName, email);
        reg.fillAccountDetails("Passw0rd!");
        reg.submitAccount();
        
        // Should either succeed or show appropriate validation message
        Assert.assertTrue(
            reg.isAccountCreated() || page.content().contains("invalid"),
            "Registration should handle special characters appropriately"
        );
    }

    @Test(description = "Navigation to registration page works correctly", priority = 5)
    @Story("UI Navigation")
    @Description("Verifies user can navigate to registration page")
    public void testNavigationToRegistrationPage() {
        String baseUrl = ConfigManager.getBaseUrl();
        HomePage home = new HomePage(page);
        home.navigate(baseUrl);
        
        TestListeners.currentPage.set(page);
        
        // Verify home page loaded
        Assert.assertTrue(page.url().contains("automationexercise.com"),
            "Should be on automationexercise.com");
        
        home.goToSignupLogin();
        
        // Verify navigation to signup page
        Assert.assertTrue(
            page.url().contains("signup") || page.url().contains("login"),
            "Should navigate to signup/login page"
        );
        
        // Verify signup form elements are visible
        Assert.assertTrue(
            page.locator("input[data-qa='signup-name']").isVisible(),
            "Signup name field should be visible"
        );
        Assert.assertTrue(
            page.locator("input[data-qa='signup-email']").isVisible(),
            "Signup email field should be visible"
        );
    }
}
