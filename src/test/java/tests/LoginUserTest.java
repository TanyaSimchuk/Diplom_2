package tests;

import edu.praktikum.models.UserCreate;
import edu.praktikum.user.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static edu.praktikum.user.UserRandom.randomUserCreate;
import static org.junit.Assert.assertEquals;

public class LoginUserTest {
    private UserCreate userCreate;
    private UserClient userClient;
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;

        userCreate = randomUserCreate();
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Succesful login user")
    @Description("Успешная авторизация созданного юзера")
    public void loginUser() {
        userClient.create(userCreate);
        Response loginResponse = userClient.loginUser(userCreate);
        assertEquals("Неверный статус код", HttpStatus.SC_OK, loginResponse.statusCode());
        assertEquals("Неверное сообщение об авторизации юзера", true, loginResponse.path("success"));
    }

    @Test
    @DisplayName("Wrong data login user")
    @Description("Авторизация юзера с неверными данными")
    public void loginWrongUser() {
        Response loginWrongUserResponse = userClient.loginUser(randomUserCreate());
        assertEquals("Неверный статус код", HttpStatus.SC_UNAUTHORIZED, loginWrongUserResponse.statusCode());
        assertEquals("Неверное сообщение об ошибке", "email or password are incorrect", loginWrongUserResponse.path("message"));
    }
    @After
    public void deleteUser() {
        userClient.create(userCreate);
        String token = userClient.loginUser(userCreate).path("accessToken");
        int Response = userClient.loginUser(userCreate).statusCode();
        if (Response == HttpStatus.SC_OK) {
            userClient.deleteByToken(token);
        }
    }
}
