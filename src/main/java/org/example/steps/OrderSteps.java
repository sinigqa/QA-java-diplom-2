package org.example.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.example.config.Endpoints;
import org.example.model.Order;
import static io.restassured.RestAssured.given;

public class OrderSteps {
    @Step("Создание заказа")
    public ValidatableResponse createOrder(Order order, String accessToken) {
        RequestSpecification request = given()
                .body(order);

        if (accessToken != null) {
            request = request.header("Authorization", accessToken);
        }

        return request
                .post(Endpoints.Order.CREATE)
                .then();
    }
}