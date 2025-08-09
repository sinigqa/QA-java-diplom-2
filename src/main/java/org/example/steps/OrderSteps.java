package org.example.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.example.config.Endpoints;
import org.example.model.Order;

import java.util.List;

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

    @Step("Получение ID ингредиентов")
    public List<String> getAllIngredientIds() {
        return given()
                .get(Endpoints.Ingredient.GET_ALL)
                .then()
                .extract()
                .jsonPath()
                .getList("data._id");
    }
}