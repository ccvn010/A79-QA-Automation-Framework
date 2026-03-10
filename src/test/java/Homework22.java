package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.BasePage;

import java.util.UUID;

public class Homework22 extends BasePage {   // BasePage gives driver & wait

    private LoginPage loginPage;
    private HomePage homePage;

    private final String originalPlaylistName = "Playlist_" + UUID.randomUUID().toString().substring(0, 8);
    private final String newPlaylistName = "Renamed_" + UUID.randomUUID().toString().substring(0, 8);

    public Homework22() {
        super(null); // temporary – driver will be set in @BeforeMethod
    }

    @BeforeMethod
    @Parameters("baseUrl")
    public void setUp(String baseUrl) {
        // driver is inherited from BasePage – but we need to initialize it
        // In a real framework, driver setup would be in a separate test base.
        // For simplicity, we assume the driver is already instantiated (e.g., from a BaseTest).
        // Here we'll just navigate; driver should be set before this.
        driver.get(baseUrl);
        loginPage = new LoginPage(driver);
    }

    @Test
    @Parameters("baseUrl")
    public void renamePlaylist(String baseUrl) {
        // 1. Login
        homePage = loginPage.login("your_email@example.com", "your_password");

        // 2. Create a playlist if it doesn't exist
        if (!homePage.playlistExists(originalPlaylistName)) {
            homePage.createPlaylist(originalPlaylistName);
        }

        // 3. Rename the playlist
        homePage.renamePlaylist(originalPlaylistName, newPlaylistName);

        // 4. Verify notification
        String actualMessage = getNotificationText();
        String expectedMessage = "Updated playlist " + newPlaylistName; // adjust as needed

        Assert.assertEquals(actualMessage, expectedMessage,
                "Rename notification mismatch");
    }
}

