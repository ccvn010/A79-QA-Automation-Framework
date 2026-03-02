import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class LoginTests extends BaseTest {

    @Test
    public void loginValidEmailValidPassword() {
        // Positive test
        getDriver().get("https://qa.koel.app/");
        LoginPage loginPage = new LoginPage(getDriver());
        HomePage homePage = loginPage.login("valid@email.com", "validPass");
        assertTrue(homePage.isUserLoggedIn(), "Login failed with valid credentials");
    }

    @Test
    public void loginEmptyEmail() {
        // Negative test – empty email
        getDriver().get("https://qa.koel.app/");
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.enterEmail("");
        loginPage.enterPassword("somePass");
        loginPage.clickLoginButton();
        assertTrue(loginPage.isErrorMessageDisplayed(), "Error message not shown for empty email");
    }

    @Test
    public void loginEmptyPassword() {
        // Negative test – empty password
        // similar implementation
    }

    @Test
    public void loginInvalidCredentials() {
        // Negative test – wrong email/password
    }
}
