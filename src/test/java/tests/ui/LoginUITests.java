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
import pages.LoginPage;
import utils.ConfigManager;
import utils.TestListeners;

/**
 * UI test suite for User Login functionality
 * Tests cover positive and negative login scenarios
 */
@Listeners({TestListeners.class})
@Epic("UI")
@Feature("Login")
public class LoginUITests extends BaseTest {

    @Test(description = "Login with invalid password shows proper error", priority = 1)
    @Story("Negative Login Scenarios")
    @Description("Negative login validation for incorrect password message.")
    public void testInvalidLogin() {
        String baseUrl = ConfigManager.getBaseUrl();
        HomePage home = new HomePage(page);
        
        // Step 1: Navigate and verify home page
        home.navigate(baseUrl);
        Assert.assertTrue(home.verifyHomePage(), 
            "Home page should be displayed");
        
        // Step 2: Go to login page
        home.goToSignupLogin();
        TestListeners.currentPage.set(page);
        
        // Step 3: Verify login page
        LoginPage login = new LoginPage(page);
        Assert.assertTrue(login.verifyLoginPage(), 
            "Login page should be displayed");
        
        // Step 4: Attempt login with invalid credentials
        login.fillEmail("not-existing@testmail.com");
        login.fillPassword("wrongpassword123");
        login.submitLogin();
        
        // Step 5: Verify login failure
        Assert.assertTrue(login.verifyLoginFailure(), 
            "Expected error message for incorrect credentials");
    }
}
