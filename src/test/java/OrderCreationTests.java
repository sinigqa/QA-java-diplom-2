import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.example.model.Order;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class OrderCreationTests extends BaseTest {

    @Before
    public void setUpUser() {
        user = generateRandomUser();
        accessToken = userSteps.createUser(user)
                .extract().path("accessToken");
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Создание заказа авторизованным пользователем")
    public void testCreateOrderWithAuth() {
        accessToken = userSteps.loginUser(user).extract().path("accessToken");
        Order order = generateRandomOrder();
        orderSteps.createOrder(order, accessToken)
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("name", notNullValue())
                .body("order.number", greaterThan(0));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Попытка создания заказа без авторизации")
    public void testCreateOrderWithoutAuth() {
        Order order = generateRandomOrder();
        orderSteps.createOrder(order, null)
                .statusCode(SC_UNAUTHORIZED)
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами")
    @Description("Создание заказа с валидными ингредиентами")
    public void testCreateOrderWithIngredients() {
        accessToken = userSteps.loginUser(user).extract().path("accessToken");
        Order order = generateRandomOrder();
        orderSteps.createOrder(order, accessToken)
                .statusCode(SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Попытка создания заказа без передачи ингредиентов")
    public void testCreateOrderWithoutIngredients() {
        accessToken = userSteps.loginUser(user).extract().path("accessToken");
        Order order = new Order();
        order.setIngredients(new java.util.ArrayList<>());

        orderSteps.createOrder(order, accessToken)
                .statusCode(SC_BAD_REQUEST)
                .body("success", is(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Попытка создания заказа с несуществующим хешем ингредиента")
    public void testCreateOrderWithInvalidIngredientHash() {
        accessToken = userSteps.loginUser(user).extract().path("accessToken");
        Order order = new Order();
        java.util.List<String> ingredients = new java.util.ArrayList<>();
        ingredients.add("invalid_hash_12345"); // невалидный ID
        order.setIngredients(ingredients);

        orderSteps.createOrder(order, accessToken)
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}