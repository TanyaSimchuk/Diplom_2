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

import static edu.praktikum.user.UserRandom.randomUserCreate;
import static org.junit.Assert.assertEquals;

public class GetOrdersTest {
    private UserCreate userCreate;
    private UserClient userClient;
    private OrderMap orderMap;
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;

        userCreate = randomUserCreate();
        userClient = new UserClient();
        orderMap = new OrderMap();
    }

    @Test
    @DisplayName("Get orders of authorized user")
    @Description("Получение списка заказов авторизованного юзера")
    public void getOrdersAuthorizedUser() {
        userClient.create(userCreate);
        String token = userClient.loginUser(userCreate).path("accessToken");
        Response getOrdersWithAuth = orderMap.getOrders(token);
        assertEquals("Неверный статус код", HttpStatus.SC_OK, getOrdersWithAuth.statusCode());
        assertEquals("Неверное сообщение о получении списка заказов", true, getOrdersWithAuth.path("success"));
    }
    @Test
    @DisplayName("Get orders of unauthorized user")
    @Description("Получение списка заказов неавторизованного юзера")
    public void getOrdersWithoutAuth() {
        Response getOrdersWithoutAuth = orderMap.getOrders("");
        assertEquals("Неверный статус код", HttpStatus.SC_UNAUTHORIZED, getOrdersWithoutAuth.statusCode());
        assertEquals("Неверное сообщение об ошибке", "You should be authorised", getOrdersWithoutAuth.path("message"));
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
