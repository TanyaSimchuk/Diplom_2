package tests;

import edu.praktikum.models.UserCreate;
import edu.praktikum.user.UserClient;
import edu.praktikum.user.UserRandom;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static edu.praktikum.user.UserRandom.randomUserCreate;
import static org.junit.Assert.assertEquals;

public class ChangeDataUserTest {
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
    @DisplayName("Change data of authorized user")
    @Description("Смена данных авторизованного юзера")
    public void changeDataUser() {
        userClient.create(userCreate);
        String token = userClient.loginUser(userCreate).path("accessToken");
        Response changeDataResponse = userClient.patchByToken(token, UserRandom.randomUserCreate());
        assertEquals("Неверный статус код", HttpStatus.SC_OK, changeDataResponse.statusCode());
        assertEquals("неверное сообщение после смены данных", true, changeDataResponse.path("success"));
    }
    @Test
    @DisplayName("Change data of unauthorized user")
    @Description("Смена данных неавторизованного юзера")
    public void changeDataWithoutAuth() {
        Response changeWithoutAuth = userClient.patchByToken("", UserRandom.randomUserCreate());
        assertEquals("Неверный статус код", HttpStatus.SC_UNAUTHORIZED, changeWithoutAuth.statusCode());
        assertEquals("Неверное сообщение об ошибке", "You should be authorised", changeWithoutAuth.path("message"));
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
