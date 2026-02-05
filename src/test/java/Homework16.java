import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.Assert;

public class Homework16 {
    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        // Set the path to your ChromeDriver (Update this path for your system!)
        System.setProperty("webdriver.chrome.driver", "/path/to/your/chromedriver");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void registrationNavigation() {
        // 1. Navigate to "https://qa.koel.app/"
        driver.get("https://qa.koel.app/");

        // 2. Click the Registration link
        // The link text is "Registration" as seen on the page
        WebElement registrationLink = driver.findElement(By.linkText("Registration"));
        registrationLink.click();

        // 3. Verify that you are redirected to the Registration page
        // We can assert that the current URL is no longer the login page
        String currentUrl = driver.getCurrentUrl();
        // Check if the URL changed from the homepage (common pattern)
        Assert.assertNotEquals(currentUrl, "https://qa.koel.app/", "Did not navigate away from the home page.");
        // Often, you can also check if the new URL contains a keyword like 'registration'
        // Assert.assertTrue(currentUrl.contains("registration"), "Not on the registration page.");
    }

    @AfterMethod
    public void tearDown() {
        // Close the browser
        driver.quit();
    }
}
