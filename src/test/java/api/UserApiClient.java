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
        Response res = RestAssured.given()
            .formParam("name", name)
            .formParam("email", email)
            .formParam("password", password)
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
