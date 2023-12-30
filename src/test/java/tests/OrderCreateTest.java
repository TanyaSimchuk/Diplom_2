package tests;

import edu.praktikum.models.UserCreate;
import edu.praktikum.order.OrderMap;
import edu.praktikum.user.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static edu.praktikum.user.UserRandom.randomUserCreate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OrderCreateTest {
    private UserCreate userCreate;
    private UserClient userClient;
    private OrderMap orderMap;
    private static final File INGREDIENTS = new File("src/test/resources/ingredients.json");
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;

        userCreate = randomUserCreate();
        userClient = new UserClient();
        orderMap = new OrderMap();
    }
    @Test
    @DisplayName("Create an order by authorized user")
    @Description("Создание заказа авторизованным юзером")
    public void createOrderAuthorizedUser(){
        userClient.create(userCreate);
        String token = userClient.loginUser(userCreate).path("accessToken");
        Response createOrderResponse = orderMap.createOrder(token, INGREDIENTS);
        assertEquals("Неверный статус код", HttpStatus.SC_OK, createOrderResponse.statusCode());
        assertEquals("Неверное сообщение о создании заказа", true, createOrderResponse.path("success"));
    }
    @Test
    @DisplayName("Create an order without auth")
    @Description("Создание заказа без авторизации")
    public void createOrderWithoutAuth() {
        Response createOrderWithoutAuth = orderMap.createOrder("", INGREDIENTS);
        assertEquals("Неверный статус код", HttpStatus.SC_OK, createOrderWithoutAuth.statusCode());
        assertNull("Неверное сообщение о наполнении заказа", createOrderWithoutAuth.path("price"));
    }
    @Test
    @DisplayName("Create an order without body")
    @Description("Создание заказа без ингредиентов")
    public void createOrderWithoutIngrds() {
        userClient.create(userCreate);
        String token = userClient.loginUser(userCreate).path("accessToken");
        Response createOrderWithoutIngrds = orderMap.createOrder(token, new File("src/test/resources/empty.json"));
        assertEquals("Неверный статус код", HttpStatus.SC_BAD_REQUEST, createOrderWithoutIngrds.statusCode());
        assertEquals("Неверное сообщение об ошибке", "Ingredient ids must be provided", createOrderWithoutIngrds.path("message"));
    }
    @Test
    @DisplayName("Create an order with wrong hash of ingrds")
    @Description("Создание заказа с неверным хешем ингредиентов")
    public void createWithWrongHash() {
        userClient.create(userCreate);
        String token = userClient.loginUser(userCreate).path("accessToken");
        Response createWithWrongHash = orderMap.createOrder(token, new File("src/test/resources/wrongHash.json"));
        assertEquals("Неверный статус код", HttpStatus.SC_INTERNAL_SERVER_ERROR, createWithWrongHash.statusCode());
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
