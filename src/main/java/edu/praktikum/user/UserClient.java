package edu.praktikum.user;

import edu.praktikum.models.UserCreate;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserClient {
    private static final String USER_CREATE = "/api/auth/register";
    private static final String USER_LOGIN = "/api/auth/login";
    private static final String USER_API = "api/auth/user";

    @Step("Creating a new user")
    public Response create(UserCreate userCreate) {
        return given()
                .header("Content-Type", "application/json")
                .and()
                .body(userCreate)
                .when()
                .post(USER_CREATE);
    }

    @Step("Login a user")
    public Response loginUser(UserCreate userCreate) {
        return given()
                .header("Content-Type", "application/json")
                .and()
                .body(userCreate)
                .when()
                .post(USER_LOGIN);
    }

    @Step("Delete user")
    public Response deleteByToken(String token) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .post(USER_API);
    }

    @Step("Change user data")
    public Response patchByToken(String authToken, UserCreate userCreate) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", authToken)
                .and()
                .body(userCreate)
                .patch(USER_API);
    }
}
