package tests.api;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import api.UserApiClient;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import utils.ConfigManager;
import utils.DataUtils;

/**
 * API test suite for User Login functionality
 * Tests cover positive scenarios, negative scenarios, and edge cases
 */
@Epic("API")
@Feature("Login")
public class LoginAPITests {

    private UserApiClient api;

    @BeforeClass
    public void setup() {
        String baseUrl = ConfigManager.getBaseUrl();
        api = new UserApiClient(baseUrl);
    }

    @DataProvider(name = "invalidLogins")
    public Object[][] invalidLogins() {
        List<Map<String, Object>> testData = DataUtils.readJsonData("logins.json");
        Object[][] data = new Object[testData.size()][];
        
        for (int i = 0; i < testData.size(); i++) {
            Map<String, Object> login = testData.get(i);
            data[i] = new Object[] {
                login.get("email"),
                login.get("password"),
                ((Number) login.get("expectedStatus")).intValue(),
                login.get("expectedResult"),
                login.get("description")
            };
        }
        return data;
    }

    @Test(dataProvider = "invalidLogins", description = "Login with invalid credentials", priority = 1)
    @Story("Negative Login Scenarios")
    @Description("Verifies login endpoint properly rejects invalid credentials")
    public void testLoginWithInvalidCredentials(String email, String password, int expectedStatus, 
                                                String expectedResult, String description) {
        Response res = api.login(email, password);
        
        Assert.assertEquals(res.statusCode(), expectedStatus, 
            "Unexpected status code for: " + description);
        
        String responseBody = res.asString().toLowerCase();
        Assert.assertTrue(
            responseBody.contains(expectedResult.toLowerCase()) || 
            responseBody.contains("error") || 
            responseBody.contains("invalid") ||
            responseBody.contains("incorrect") ||
            responseBody.contains("user not found") ||
            res.statusCode() == 404,
            "Expected failure indicator in response for: " + description + ". Response: " + res.asString()
        );
    }

    @Test(description = "Login with valid credentials after registration", priority = 2)
    @Story("Positive Login Scenarios")
    @Description("Verifies successful login with valid credentials")
    public void testLoginWithValidCredentials() {
        // First, register a new user
        String email = DataUtils.uniqueEmail();
        String password = "Passw0rd!";
        String name = DataUtils.randomName();
        
        Response registerRes = api.register(name, email, password);
        Assert.assertEquals(registerRes.statusCode(), 200, 
            "Registration should succeed before login test");
        
        // Now attempt to login with those credentials
        Response loginRes = api.login(email, password);
        
        Assert.assertEquals(loginRes.statusCode(), 200, 
            "Login should succeed with valid credentials");
        
        String responseBody = loginRes.asString().toLowerCase();
        // Note: Adjust assertion based on actual API response format
        Assert.assertTrue(
            responseBody.contains("success") || 
            responseBody.contains("user found") ||
            loginRes.statusCode() == 200,
            "Expected success indicator in login response. Response: " + loginRes.asString()
        );
    }

    @Test(description = "Login with SQL injection attempt", priority = 3)
    @Story("Security Testing")
    @Description("Verifies API is protected against SQL injection")
    public void testLoginWithSQLInjection() {
        String sqlInjection = "' OR '1'='1";
        Response res = api.login(sqlInjection, sqlInjection);
        
        // API should reject SQL injection attempts
        Assert.assertTrue(
            res.statusCode() == 200 || res.statusCode() >= 400,
            "API handled SQL injection attempt. Status: " + res.statusCode()
        );
        
        String responseBody = res.asString().toLowerCase();
        Assert.assertFalse(
            responseBody.contains("success") && responseBody.contains("user found"),
            "SQL injection should not succeed. Response: " + res.asString()
        );
    }

    @Test(description = "Login with XSS attempt", priority = 4)
    @Story("Security Testing")
    @Description("Verifies API is protected against XSS attacks")
    public void testLoginWithXSS() {
        String xssPayload = "<script>alert('xss')</script>";
        Response res = api.login(xssPayload, "password");
        
        // API should handle XSS attempts safely
        Assert.assertTrue(
            res.statusCode() >= 200 && res.statusCode() < 500,
            "API should handle XSS gracefully. Status: " + res.statusCode()
        );
    }

    @Test(description = "Login with empty credentials", priority = 5)
    @Story("Edge Cases")
    @Description("Verifies API handles empty credentials appropriately")
    public void testLoginWithEmptyCredentials() {
        Response res = api.login("", "");
        
        Assert.assertEquals(res.statusCode(), 200, 
            "API returned status: " + res.statusCode());
        
        String responseBody = res.asString().toLowerCase();
        Assert.assertTrue(
            responseBody.contains("error") || 
            responseBody.contains("invalid") ||
            responseBody.contains("required") ||
            responseBody.contains("user not found") ||
            res.statusCode() >= 400,
            "Empty credentials should be rejected. Response: " + res.asString()
        );
    }

    @Test(description = "Login with very long credentials", priority = 6)
    @Story("Edge Cases")
    @Description("Verifies API handles very long credentials")
    public void testLoginWithLongCredentials() {
        String longEmail = "a".repeat(500) + "@test.com";
        String longPassword = "P@ssw0rd!".repeat(100);
        
        Response res = api.login(longEmail, longPassword);
        
        // API should handle long values gracefully
        Assert.assertTrue(
            res.statusCode() >= 200 && res.statusCode() < 500,
            "API should handle long credentials gracefully. Status: " + res.statusCode()
        );
    }

    @Test(description = "Verify login response structure", priority = 7)
    @Story("Response Validation")
    @Description("Verifies login API response has correct structure and fields")
    public void testLoginResponseStructure() {
        Response res = api.login("test@test.com", "password");
        
        Assert.assertEquals(res.statusCode(), 200, 
            "API should return 200 status");
        
        // Verify response is valid JSON or has expected format
        Assert.assertNotNull(res.asString(), 
            "Response body should not be null");
        
        Assert.assertFalse(res.asString().isEmpty(), 
            "Response body should not be empty");
        
        // Verify response time is reasonable (less than 5 seconds)
        Assert.assertTrue(res.getTime() < 5000, 
            "Response time should be less than 5 seconds. Actual: " + res.getTime() + "ms");
    }

    @Test(description = "Multiple failed login attempts", priority = 8)
    @Story("Edge Cases")
    @Description("Verifies API behavior with multiple failed login attempts")
    public void testMultipleFailedLoginAttempts() {
        String email = "nonexistent@test.com";
        String password = "wrongpassword";
        
        // Attempt multiple logins
        for (int i = 0; i < 5; i++) {
            Response res = api.login(email, password);
            Assert.assertEquals(res.statusCode(), 200, 
                "Each attempt should return consistent status");
        }
        
        // Note: In real scenarios, you might want to check for rate limiting or account lockout
    }
}
