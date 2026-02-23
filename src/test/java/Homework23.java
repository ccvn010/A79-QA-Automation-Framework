package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.HomePage;
import pages.LoginPage;
import java.time.Duration;
import java.util.UUID;

public class Homework23 {
    private WebDriver driver;
    private LoginPage loginPage;
    private HomePage homePage;
    private final String originalPlaylistName = "Playlist_" + UUID.randomUUID().toString().substring(0, 8);
    private final String newPlaylistName = "Renamed_" + UUID.randomUUID().toString().substring(0, 8);

    @BeforeMethod
    @Parameters("baseUrl")
    public void setUp(String baseUrl) {
        System.setProperty("webdriver.chrome.driver", "/path/to/your/chromedriver");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(baseUrl);
        loginPage = new LoginPage(driver);
    }

    @Test
    @Parameters("baseUrl")
    public void renamePlaylist() {
        homePage = loginPage.loginAs("your_email@example.com", "your_password");
        if (!homePage.playlistExists(originalPlaylistName)) {
            homePage.createPlaylist(originalPlaylistName);
        }
        homePage.renamePlaylist(originalPlaylistName, newPlaylistName);
        String actualMessage = homePage.getNotificationText();
        String expectedMessage = "Updated playlist " + newPlaylistName; // adjust as needed
        Assert.assertEquals(actualMessage, expectedMessage);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}
