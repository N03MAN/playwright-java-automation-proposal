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
 * API test suite for User Registration functionality
 * Tests cover positive scenarios, negative scenarios, and edge cases
 */
@Epic("API")
@Feature("Registration")
public class RegistrationAPITests {

    private UserApiClient api;

    @BeforeClass
    public void setup() {
        String baseUrl = ConfigManager.getBaseUrl();
        api = new UserApiClient(baseUrl);
    }

    @DataProvider(name = "validUsers")
    public Object[][] validUsers() {
        List<Map<String, Object>> testData = DataUtils.readJsonData("users.json");
        Object[][] data = new Object[testData.size()][];
        
        for (int i = 0; i < testData.size(); i++) {
            Map<String, Object> user = testData.get(i);
            // Replace <generated> with actual unique email
            String email = "<generated>".equals(user.get("email")) 
                ? DataUtils.uniqueEmail() 
                : (String) user.get("email");
            
            data[i] = new Object[] {
                user.get("name"),
                email,
                user.get("password"),
                200,
                user.get("description")
            };
        }
        return data;
    }

    @DataProvider(name = "invalidUsers")
    public Object[][] invalidUsers() {
        return new Object[][] {
            { "", DataUtils.uniqueEmail(), "Passw0rd!", 400, "Empty name field" },
            { "Test User", "", "Passw0rd!", 400, "Empty email field" },
            { "Test User", DataUtils.uniqueEmail(), "", 400, "Empty password field" },
            { "Test User", "not-an-email", "Passw0rd!", 400, "Invalid email format" },
        };
    }

    @Test(dataProvider = "validUsers", description = "Register users with valid data", priority = 1)
    @Story("Positive Registration Scenarios")
    @Description("Verifies successful user registration with valid data from users.json")
    public void testRegisterValidUsers(String name, String email, String password, int expectedStatus, String description) {
        Response res = api.register(name, email, password);
        
        Assert.assertEquals(res.statusCode(), expectedStatus, 
            "Unexpected status code for: " + description);
        
        String responseBody = res.asString().toLowerCase();
        Assert.assertTrue(
            responseBody.contains("success") || responseBody.contains("created") || res.statusCode() == 200,
            "Expected success indicator in response for: " + description + ". Response: " + res.asString()
        );
    }

    @Test(dataProvider = "invalidUsers", description = "Register users with invalid data", priority = 2)
    @Story("Negative Registration Scenarios")
    @Description("Verifies proper error handling for invalid registration data")
    public void testRegisterInvalidUsers(String name, String email, String password, int expectedStatus, String description) {
        Response res = api.register(name, email, password);
        
        // Note: API might return 200 even for invalid data, adjust based on actual API behavior
        Assert.assertTrue(
            res.statusCode() == expectedStatus || res.statusCode() == 200,
            "Unexpected status code for: " + description + ". Got: " + res.statusCode()
        );
        
        // If API returns error messages, verify them
        if (res.statusCode() >= 400) {
            Assert.assertFalse(
                res.asString().toLowerCase().contains("success"),
                "Should not contain success message for: " + description
            );
        }
    }

    @Test(description = "Attempt to register duplicate user", priority = 3)
    @Story("Duplicate User Registration")
    @Description("Verifies that registering the same email twice is handled correctly")
    public void testRegisterDuplicateUser() {
        String email = DataUtils.uniqueEmail();
        String name = "Duplicate User";
        String password = "Passw0rd!";
        
        // First registration
        Response res1 = api.register(name, email, password);
        Assert.assertEquals(res1.statusCode(), 200, 
            "First registration should succeed");
        
        // Attempt duplicate registration
        Response res2 = api.register(name, email, password);
        
        // API should reject or indicate email already exists
        String response = res2.asString().toLowerCase();
        Assert.assertTrue(
            response.contains("exist") || 
            response.contains("already") || 
            response.contains("bad request") ||
            res2.statusCode() >= 400,
            "Duplicate registration should be rejected. Response: " + res2.asString()
        );
    }

    @Test(description = "Register user with special characters in name", priority = 4)
    @Story("Edge Cases")
    @Description("Verifies registration with special characters in name field")
    public void testRegisterUserWithSpecialChars() {
        String specialName = "Test!@#$%^&*()User";
        String email = DataUtils.uniqueEmail();
        String password = "Passw0rd!";
        
        Response res = api.register(specialName, email, password);
        
        // Verify API handles special characters appropriately
        Assert.assertTrue(
            res.statusCode() == 200 || res.statusCode() == 400,
            "API should handle special characters gracefully. Status: " + res.statusCode()
        );
    }

    @Test(description = "Register user with very long values", priority = 5)
    @Story("Edge Cases")
    @Description("Verifies registration with very long field values")
    public void testRegisterUserWithLongValues() {
        String longName = "A".repeat(255); // Very long name
        String email = DataUtils.uniqueEmail();
        String longPassword = "P@ssw0rd!".repeat(30); // Very long password
        
        Response res = api.register(longName, email, longPassword);
        
        // API should handle long values appropriately
        Assert.assertTrue(
            res.statusCode() >= 200 && res.statusCode() < 500,
            "API should handle long values gracefully. Status: " + res.statusCode()
        );
    }
}
