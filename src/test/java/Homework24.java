import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;

public class Homework24 {

    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        // Obtain a Chrome session from the local Grid
        driver = BrowserFactory.getDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void testKoelLoginPage() {
        driver.get("https://qa.koel.app/");
        String title = driver.getTitle();
        // You can replace with a more meaningful assertion
        assertTrue(title.contains("Koel") || driver.getPageSource().contains("Log In"),
                   "Koel page did not load correctly");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
