import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class LoginTests extends BaseTest {

    @Test
    public void loginValidEmailValidPassword() {
        // Positive test
        getDriver().get("https://qa.koel.app/");
        LoginPage loginPage = new LoginPage(getDriver());
        HomePage homePage = loginPage.login("your_email@example.com", "your_password");
        assertTrue(homePage.isUserLoggedIn(), "Login failed");
    }

    @Test
    public void loginEmptyEmail() {
        getDriver().get("https://qa.koel.app/");
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.enterEmail("");
        loginPage.enterPassword("somePass");
        loginPage.clickLoginButton();
        assertTrue(loginPage.isErrorDisplayed(), "Error not shown");
    }

    @Test
    public void loginEmptyPassword() {
        // Similar implementation
    }

    @Test
    public void loginInvalidCredentials() {
        // Similar
    }
}
