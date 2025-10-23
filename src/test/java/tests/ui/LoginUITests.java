package tests.ui;

import java.util.Map;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import api.UserApiClient;
import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import pages.HomePage;
import pages.LoginPage;
import utils.AssertionHelper;
import utils.ConfigManager;
import utils.TestDataManager;
import utils.TestListeners;

/**
 * Login UI Tests - Single Responsibility: User Login Testing
 * Tests the user login flow through the UI with various scenarios
 * Utilizes Data-Driven Testing (DDT) with external JSON files
 */
@Listeners({TestListeners.class})
@Epic("UI Testing")
@Feature("User Login")
public class LoginUITests extends BaseTest {
    
    /**
     * Data Provider for valid login scenarios
     * Loads test data from external JSON file (Data-Driven Testing)
     */
    @DataProvider(name = "validLogins")
    public Object[][] validLoginData() {
        return TestDataManager.toDataProvider("logins.json");
    }
    
    /**
     * Data Provider for invalid login scenarios
     * Generates various invalid login combinations
     */
    @DataProvider(name = "invalidLogins")
    public Object[][] invalidLoginData() {
        return new Object[][] {
            { "nonexistent@test.com", "wrongpass", "Non-existent user login" },
            { "test@test.com", "", "Empty password" },
            { "", "password", "Empty email" },
            { "invalid-email", "password", "Invalid email format" },
            { "test@test.com", "short", "Too short password" }
        };
    }
    
    /**
     * Test Case: Valid user login
     * Ensures user exists via API, then tests UI login
     */
    @Test(dataProvider = "validLogins", priority = 1)
    @Story("Valid Login Flow")
    @Description("Verify users can login successfully with valid credentials from logins.json")
    public void testValidLogin(Map<String, Object> loginData) {
        // Extract test data
        String email = (String) loginData.get("email");
        String password = (String) loginData.get("validPassword");
        String userName = (String) loginData.get("name");
        
        // Ensure user exists (prerequisite setup)
        ensureUserExists(email, password, userName);
        
        // Page objects
        HomePage homePage = new HomePage(page);
        LoginPage loginPage = new LoginPage(page);
        
        // Execute login flow
        navigateToHomePage(homePage);
        navigateToLogin(homePage);
        performLogin(loginPage, email, password);
        verifySuccessfulLogin(loginPage, homePage, userName);
    }
    
    /**
     * Test Case: Invalid login attempts
     * Data-driven test for various invalid credentials
     */
    @Test(dataProvider = "invalidLogins", priority = 2)
    @Story("Invalid Login Validation")
    @Description("Verify system properly handles invalid login attempts with appropriate error messages")
    public void testInvalidLogin(String email, String password, String scenario) {
        // Page objects
        HomePage homePage = new HomePage(page);
        LoginPage loginPage = new LoginPage(page);
        
        // Execute invalid login
        navigateToHomePage(homePage);
        navigateToLogin(homePage);
        performLogin(loginPage, email, password);
        
        // Verify error handling
        verifyLoginFailure(loginPage, scenario);
    }
    
    /**
     * Test Case: Login with wrong password for existing user
     * Tests password validation specifically
     */
    @Test(priority = 3)
    @Story("Password Validation")
    @Description("Verify system rejects login with incorrect password for existing user")
    public void testWrongPassword() {
        // Test data from JSON
        Map<String, Object> loginData = TestDataManager.getTestDataAt("logins.json", 0);
        String email = (String) loginData.get("email");
        String validPassword = (String) loginData.get("validPassword");
        String invalidPassword = (String) loginData.get("invalidPassword");
        String userName = (String) loginData.get("name");
        
        // Ensure user exists
        ensureUserExists(email, validPassword, userName);
        
        // Page objects
        HomePage homePage = new HomePage(page);
        LoginPage loginPage = new LoginPage(page);
        
        // Try login with wrong password
        navigateToHomePage(homePage);
        navigateToLogin(homePage);
        performLogin(loginPage, email, invalidPassword);
        
        // Verify specific error
        AssertionHelper.assertCondition(
            loginPage.verifyLoginFailure(),
            "Login fails with wrong password",
            String.format("Expected login to fail for user '%s' with wrong password, " +
                "but no error message was shown. Security validation may be broken.", email)
        );
    }
    
    /**
     * Test Case: Login persistence check
     * Verifies login state is maintained across navigation
     */
    @Test(priority = 4)
    @Story("Session Persistence")
    @Description("Verify login session persists when navigating through the site")
    public void testLoginPersistence() {
        // Test data
        Map<String, Object> loginData = TestDataManager.getTestDataAt("logins.json", 0);
        String email = (String) loginData.get("email");
        String password = (String) loginData.get("validPassword");
        String userName = (String) loginData.get("name");
        
        // Ensure user exists and login
        ensureUserExists(email, password, userName);
        
        HomePage homePage = new HomePage(page);
        LoginPage loginPage = new LoginPage(page);
        
        // Login
        navigateToHomePage(homePage);
        navigateToLogin(homePage);
        performLogin(loginPage, email, password);
        verifySuccessfulLogin(loginPage, homePage, userName);
        
        // Navigate away and back
        homePage.navigate(ConfigManager.getBaseUrl() + "/products");
        page.waitForTimeout(1000);
        homePage.navigate(ConfigManager.getBaseUrl());
        
        // Verify still logged in
        AssertionHelper.assertCondition(
            homePage.verifyLoggedIn(userName),
            "Login persists after navigation",
            "Login session was lost after navigation. Session management may have issues."
        );
    }
    
    /**
     * Test Case: Multiple failed login attempts
     * Tests account lockout or rate limiting behavior
     */
    @Test(priority = 5)
    @Story("Security - Failed Attempts")
    @Description("Verify system handles multiple failed login attempts appropriately")
    public void testMultipleFailedAttempts() {
        String email = "bruteforce@test.com";
        String wrongPassword = "wrongpass123";
        
        HomePage homePage = new HomePage(page);
        LoginPage loginPage = new LoginPage(page);
        
        // Attempt multiple failed logins
        for (int i = 1; i <= 3; i++) {
            navigateToHomePage(homePage);
            navigateToLogin(homePage);
            performLogin(loginPage, email, wrongPassword);
            
            AssertionHelper.softAssert(
                loginPage.verifyLoginFailure(),
                String.format("Failed login attempt %d shows error", i)
            );
        }
        
        // Verify system still responds (not locked completely)
        AssertionHelper.assertCondition(
            loginPage.verifyLoginPage(),
            "Login page still accessible after failed attempts",
            "Login page became inaccessible after multiple failed attempts. " +
            "Check if there's unexpected lockout behavior."
        );
    }
    
    // ==================== Helper Methods (Single Responsibility) ====================
    
    /**
     * Navigate to home page and verify
     */
    @Step("Navigate to home page")
    private void navigateToHomePage(HomePage homePage) {
        String baseUrl = ConfigManager.getBaseUrl();
        homePage.navigate(baseUrl);
        
        AssertionHelper.assertElementVisible(
            homePage.verifyHomePage(),
            "Home Page"
        );
    }
    
    /**
     * Navigate from home to login page
     */
    @Step("Navigate to login page")
    private void navigateToLogin(HomePage homePage) {
        homePage.goToSignupLogin();
        TestListeners.currentPage.set(page);
    }
    
    /**
     * Perform login action
     */
    @Step("Perform login with email: {email}")
    private void performLogin(LoginPage loginPage, String email, String password) {
        AssertionHelper.assertElementVisible(
            loginPage.verifyLoginPage(),
            "Login Page"
        );
        
        loginPage.login(email, password);
    }
    
    /**
     * Verify successful login
     */
    @Step("Verify successful login for user: {userName}")
    private void verifySuccessfulLogin(LoginPage loginPage, HomePage homePage, String userName) {
        AssertionHelper.assertLoginState(
            loginPage.verifyLoginSuccess(userName),
            userName,
            true
        );
        
        AssertionHelper.assertCondition(
            homePage.verifyLoggedIn(userName),
            "User is logged in on home page",
            String.format("User '%s' should be shown as logged in on home page, but is not. " +
                "Check UI state synchronization.", userName)
        );
    }
    
    /**
     * Verify login failure
     */
    @Step("Verify login failure for scenario: {scenario}")
    private void verifyLoginFailure(LoginPage loginPage, String scenario) {
        AssertionHelper.assertCondition(
            loginPage.verifyLoginFailure() || loginPage.verifyLoginPage(),
            "Login fails as expected",
            String.format("Login should fail for scenario '%s', but no error was shown. " +
                "Validation may be missing.", scenario)
        );
    }
    
    /**
     * Ensure user exists via API (test prerequisite)
     * Uses API to create user if needed, avoiding test dependencies
     */
    @Step("Ensure test user exists: {email}")
    private void ensureUserExists(String email, String password, String userName) {
        try {
            UserApiClient apiClient = new UserApiClient(ConfigManager.getBaseUrl());
            
            // Try to login via API first
            io.restassured.response.Response loginResponse = apiClient.login(email, password);
            
            // If user doesn't exist, register via API
            if (loginResponse.statusCode() == 404 || 
                !loginResponse.asString().toLowerCase().contains("user exists")) {
                
                io.restassured.response.Response registerResponse = 
                    apiClient.register(userName, email, password);
                    
                System.out.println("Test prerequisite: User registered via API - " + 
                    registerResponse.getStatusCode());
                    
                // Brief wait for registration to propagate
                page.waitForTimeout(1000);
            }
        } catch (Exception e) {
            System.err.println("Warning: Could not ensure user exists via API - " + e.getMessage());
            // Continue with test anyway
        }
    }
}