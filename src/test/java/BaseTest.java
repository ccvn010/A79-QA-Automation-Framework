import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

public class BaseTest {

    // ThreadLocal to hold one driver per thread (for parallel safety)
    protected static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    @BeforeMethod
    @Parameters({"browser", "version", "platform"})
    public void setUp(String browser, String version, String platform) {
        // Create a cloud driver using your LambdaTestFactory
        WebDriver driver = LambdaTestFactory.createDriver(browser, version, platform);
        driverThreadLocal.set(driver);
        driver.manage().window().maximize();
    }

    // Helper method for child test classes to get the driver
    protected WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    @AfterMethod
    public void tearDown() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();               // close browser session
            driverThreadLocal.remove();   // clean up ThreadLocal
        }
    }
}
