package utils;

import org.testng.Assert;

import io.qameta.allure.Step;

/**
 * Assertion Helper - Single Responsibility: Test Assertions with Meaningful Messages
 * Provides expressive assertion methods with clear failure messages
 */
public class AssertionHelper {
    
    /**
     * Assert element is visible with descriptive message
     * @param isVisible Visibility status
     * @param elementName Name of the element for error message
     */
    @Step("Verify that {elementName} is visible")
    public static void assertElementVisible(boolean isVisible, String elementName) {
        Assert.assertTrue(isVisible, 
            String.format("Expected '%s' to be visible on the page, but it was not found. " +
                "This may indicate a page loading issue or incorrect navigation.", 
                elementName));
    }
    
    /**
     * Assert element is not visible with descriptive message
     * @param isVisible Visibility status
     * @param elementName Name of the element for error message
     */
    @Step("Verify that {elementName} is not visible")
    public static void assertElementNotVisible(boolean isVisible, String elementName) {
        Assert.assertFalse(isVisible, 
            String.format("Expected '%s' to NOT be visible on the page, but it was found. " +
                "This may indicate a state issue or failed action.", 
                elementName));
    }
    
    /**
     * Assert text contains expected value with descriptive message
     * @param actualText Actual text
     * @param expectedText Expected text to contain
     * @param context Context for the assertion
     */
    @Step("Verify that text contains: {expectedText}")
    public static void assertTextContains(String actualText, String expectedText, String context) {
        Assert.assertTrue(
            actualText != null && actualText.contains(expectedText),
            String.format("In %s: Expected text to contain '%s', but actual text was '%s'. " +
                "This indicates incorrect data or failed operation.", 
                context, expectedText, actualText != null ? actualText : "null"));
    }
    
    /**
     * Assert text equals expected value with descriptive message
     * @param actualText Actual text
     * @param expectedText Expected text
     * @param context Context for the assertion
     */
    @Step("Verify that text equals: {expectedText}")
    public static void assertTextEquals(String actualText, String expectedText, String context) {
        Assert.assertEquals(actualText, expectedText, 
            String.format("In %s: Expected text to be '%s', but was '%s'. " +
                "This indicates data mismatch or incorrect state.", 
                context, expectedText, actualText));
    }
    
    /**
     * Assert condition is true with custom message
     * @param condition Condition to check
     * @param successMessage Message for successful assertion
     * @param failureMessage Detailed failure message
     */
    @Step("{successMessage}")
    public static void assertCondition(boolean condition, String successMessage, String failureMessage) {
        Assert.assertTrue(condition, failureMessage);
    }
    
    /**
     * Assert API response status code
     * @param actualStatus Actual status code
     * @param expectedStatus Expected status code
     * @param endpoint API endpoint name
     */
    @Step("Verify API {endpoint} returns status {expectedStatus}")
    public static void assertStatusCode(int actualStatus, int expectedStatus, String endpoint) {
        Assert.assertEquals(actualStatus, expectedStatus,
            String.format("API endpoint '%s' returned unexpected status code. " +
                "Expected: %d, Actual: %d. This may indicate API error or invalid request.", 
                endpoint, expectedStatus, actualStatus));
    }
    
    /**
     * Assert response contains expected field
     * @param response Response body as string
     * @param field Expected field name
     * @param endpoint API endpoint name
     */
    @Step("Verify API response contains field: {field}")
    public static void assertResponseContainsField(String response, String field, String endpoint) {
        Assert.assertTrue(
            response.contains(field),
            String.format("API endpoint '%s' response does not contain expected field '%s'. " +
                "Response: %s", endpoint, field, response));
    }
    
    /**
     * Assert login state
     * @param isLoggedIn Login status
     * @param username Expected username
     * @param shouldBeLoggedIn Expected login state
     */
    @Step("Verify user {username} login state")
    public static void assertLoginState(boolean isLoggedIn, String username, boolean shouldBeLoggedIn) {
        if (shouldBeLoggedIn) {
            Assert.assertTrue(isLoggedIn,
                String.format("Expected user '%s' to be logged in, but login was not successful. " +
                    "Check credentials, API response, or UI state.", username));
        } else {
            Assert.assertFalse(isLoggedIn,
                String.format("Expected user '%s' to NOT be logged in, but user appears logged in. " +
                    "Check logout functionality or session state.", username));
        }
    }
    
    /**
     * Assert registration state
     * @param isRegistered Registration status
     * @param username Username being registered
     * @param shouldSucceed Expected registration outcome
     */
    @Step("Verify registration for user {username}")
    public static void assertRegistrationState(boolean isRegistered, String username, boolean shouldSucceed) {
        if (shouldSucceed) {
            Assert.assertTrue(isRegistered,
                String.format("Expected registration for user '%s' to succeed, but it failed. " +
                    "Check for duplicate email, validation errors, or API issues.", username));
        } else {
            Assert.assertFalse(isRegistered,
                String.format("Expected registration for user '%s' to fail, but it succeeded. " +
                    "Check validation rules or error handling.", username));
        }
    }
    
    /**
     * Soft assert for non-critical validations
     * @param condition Condition to check
     * @param message Message to log if condition fails
     * @return boolean indicating if assertion passed
     */
    public static boolean softAssert(boolean condition, String message) {
        if (!condition) {
            System.err.println("SOFT ASSERTION FAILED: " + message);
        }
        return condition;
    }
}
