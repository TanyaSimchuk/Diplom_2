package edu.praktikum.order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.io.File;
import static io.restassured.RestAssured.given;

public class OrderMap {
    private static final String ORDERS_API = "/api/orders";

    @Step("Create an order")
    public Response createOrder(String token, File json) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .and()
                .body(json)
                .when()
                .post(ORDERS_API);
    }

    @Step("Get orders")
    public Response getOrders(String authToken) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", authToken)
                .get(ORDERS_API);
    }
}
