import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.model.User;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class UserCreationTests extends BaseTest {

    @Test
    @DisplayName("Успешное создание пользователя")
    @Description("Создание пользователя с валидными данными")
    public void testUserCanBeCreated() {
        user = generateRandomUser();
        userSteps.createUser(user)
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("user.email", equalToIgnoringCase(user.getEmail()))
                .body("user.name", equalTo(user.getName()))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Невозможно создать дубликата пользователя")
    @Description("Попытка создания пользователя с существующим email")
    public void testCannotCreateDuplicateUser() {
        user = generateRandomUser();
        userSteps.createUser(user);

        User duplicateUser = new User()
                .setEmail(user.getEmail())
                .setPassword(RandomStringUtils.randomAlphanumeric(8))
                .setName(RandomStringUtils.randomAlphabetic(8));

        userSteps.createUser(duplicateUser)
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Невозможно создать пользователя без обязательного поля email")
    @Description("Попытка создания пользователя без email")
    public void testCannotCreateUserWithoutEmail() {
        User invalidUser = new User()
                .setEmail(null)
                .setPassword(RandomStringUtils.randomAlphanumeric(8))
                .setName(RandomStringUtils.randomAlphabetic(8));

        userSteps.createUser(invalidUser)
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Невозможно создать пользователя без обязательного поля password")
    @Description("Попытка создания пользователя без password")
    public void testCannotCreateUserWithoutPassword() {
        User invalidUser = new User()
                .setEmail(RandomStringUtils.randomAlphanumeric(10) + "@test.com")
                .setPassword(null)
                .setName(RandomStringUtils.randomAlphabetic(8));

        userSteps.createUser(invalidUser)
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

}