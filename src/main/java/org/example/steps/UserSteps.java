package org.example.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.config.Endpoints;
import org.example.model.User;

import static io.restassured.RestAssured.given;

public class UserSteps {

    @Step("Создание пользователя")
    public ValidatableResponse createUser(User user){
        return given()
                .body(user)
                .when()
                .post(Endpoints.User.CREATE)
                .then();
    }

    @Step("Логинимся пользователем")
    public ValidatableResponse loginUser(User user) {
        return given()
                .body(user)
                .when()
                .post(Endpoints.User.LOGIN)
                .then();
    }
}
