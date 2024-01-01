package tests;

import edu.praktikum.models.UserCreate;
import edu.praktikum.user.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static edu.praktikum.user.UserRandom.randomUserCreate;
import static edu.praktikum.utils.RandomCores.randomString;
import static org.junit.Assert.assertEquals;

public class CreateUserTest {
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
    @DisplayName("Succesful user create")
    @Description("Успешное создание юзера")
    public void createNewUser() {
        Response createResponse = userClient.create(userCreate);
        createResponse.path("accessToken");
        assertEquals("Неверный статус код", HttpStatus.SC_OK, createResponse.statusCode());
        assertEquals("Неверное сообщение о создании юзера", true, createResponse.path("success"));
    }
    @Test
    @DisplayName("Create user with same data")
    @Description("Создание юзера с теми же данными")
    public void createSameDataUser() {
        Response firstUserCreateResponse = userClient.create(userCreate);
        assertEquals("Неверный статус код", HttpStatus.SC_OK, firstUserCreateResponse.statusCode());
        Response sameDataCreateResponse = userClient.create(userCreate);
        assertEquals("Неверный статус код", HttpStatus.SC_FORBIDDEN, sameDataCreateResponse.statusCode());
        assertEquals("Неверное сообщение об ошибке создания юзера", "User already exists", sameDataCreateResponse.path("message"));
    }
    @Test
    @DisplayName("Not full data user create")
    @Description("Создание юзера с недостаточным количеством данных")
    public void createNotFullDataUser() {
        UserCreate notFullUser = new UserCreate();
        notFullUser.withEmail(randomString(16)).withPassword(randomString(16));
        Response notFullUserResponse = userClient.create(notFullUser);
        assertEquals("Неверный статус код", HttpStatus.SC_FORBIDDEN, notFullUserResponse.statusCode());
        assertEquals("Неверное сообщение об ошибке создания юзера", "Email, password and name are required fields", notFullUserResponse.path("message"));
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
