import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.model.User;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class UserLoginTests extends BaseTest {

    @Test
    @DisplayName("Успешная авторизация")
    @Description("Вход с валидными учетными данными")
    public void testSuccessfulLogin() {
        User user = generateRandomUser();
        ValidatableResponse createResponse = userSteps.createUser(user);

        accessToken = createResponse.extract().path("accessToken");

        ValidatableResponse loginResponse = userSteps.loginUser(user);

        loginResponse
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("user.email", equalToIgnoringCase(user.getEmail()))
                .body("user.name", equalTo(user.getName()))
                .body("accessToken", notNullValue());
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    @Description("Попытка входа с неверным паролем")
    public void testLoginWithWrongPassword() {
        User user = generateRandomUser();
        userSteps.createUser(user);

        User invalidUser = new User()
                .setEmail(user.getEmail())
                .setPassword(RandomStringUtils.randomAlphanumeric(8));

        userSteps.loginUser(invalidUser)
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация с неверным email")
    @Description("Попытка входа с несуществующим email")
    public void testLoginWithWrongEmail() {
        User invalidUser = new User()
                .setEmail(RandomStringUtils.randomAlphanumeric(9) + "@test.com")
                .setPassword(RandomStringUtils.randomAlphanumeric(9));

        userSteps.loginUser(invalidUser)
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}