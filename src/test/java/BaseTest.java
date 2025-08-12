import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.config.RestConfig;
import org.example.model.Order;
import org.example.model.User;
import org.example.steps.OrderSteps;
import org.example.steps.UserSteps;
import org.junit.After;
import org.junit.Before;




public class BaseTest {
    protected final UserSteps userSteps = new UserSteps();
    protected final OrderSteps orderSteps = new OrderSteps();
    protected User user;
    protected String accessToken;

    @Before
    public void setUp() {
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri(RestConfig.HOST)
                .setContentType(ContentType.JSON)
                .build();

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());
    }

    protected User generateRandomUser() {
        return new User()
                .setEmail(RandomStringUtils.randomAlphanumeric(10) + "@test.com")
                .setPassword(RandomStringUtils.randomAlphanumeric(10))
                .setName(RandomStringUtils.randomAlphabetic(10));
    }

    protected Order generateRandomOrder() {
        return new Order()
                .setIngredients(orderSteps.getAllIngredientIds().subList(0, 2));
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken);
        }
    }
}