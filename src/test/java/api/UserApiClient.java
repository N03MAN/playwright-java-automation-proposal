package api;

import java.nio.charset.StandardCharsets;

import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.response.Response;

/**
 * API client for User-related endpoints
 * Provides methods for user registration and login operations
 * Automatically attaches API responses to Allure reports
 */
public class UserApiClient {

    /**
     * Constructs a new UserApiClient with the specified base URL
     * @param baseUrl The base URL for the API (e.g., https://www.automationexercise.com)
     */
    public UserApiClient(String baseUrl) {
        RestAssured.baseURI = baseUrl;
    }

    /**
     * Registers a new user via API
     * @param name User's full name
     * @param email User's email address
     * @param password User's password
     * @return RestAssured Response object containing status code, headers, and body
     */
    public Response register(String name, String email, String password) {
        // Extract first and last name from full name
        String[] nameParts = name.split(" ", 2);
        String firstName = nameParts.length > 0 ? nameParts[0] : name;
        String lastName = nameParts.length > 1 ? nameParts[1] : "User";
        
        Response res = RestAssured.given()
            .formParam("name", name)
            .formParam("email", email)
            .formParam("password", password)
            .formParam("title", "Mr")
            .formParam("birth_date", "1")
            .formParam("birth_month", "1")
            .formParam("birth_year", "1990")
            .formParam("firstname", firstName)
            .formParam("lastname", lastName)
            .formParam("company", "TestCompany")
            .formParam("address1", "123 Test Street")
            .formParam("address2", "Apt 1")
            .formParam("country", "United States")
            .formParam("zipcode", "12345")
            .formParam("state", "California")
            .formParam("city", "Los Angeles")
            .formParam("mobile_number", "1234567890")
            .post("/api/createAccount");
        attach(res);
        return res;
    }

    /**
     * Logs in a user via API
     * @param email User's email address
     * @param password User's password
     * @return RestAssured Response object containing status code, headers, and body
     */
    public Response login(String email, String password) {
        Response res = RestAssured.given()
            .formParam("email", email)
            .formParam("password", password)
            .post("/api/verifyLogin");
        attach(res);
        return res;
    }

    /**
     * Attaches API response to Allure report for better debugging
     * @param res The RestAssured Response object to attach
     */
    private void attach(Response res) {
        Allure.addAttachment("API Response",
                new java.io.ByteArrayInputStream(res.asPrettyString().getBytes(StandardCharsets.UTF_8)));
    }
}
