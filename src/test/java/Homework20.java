import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.UUID;

public class Homework20 extends BaseTest {

    private final String playlistName = "TestPlaylist_" + UUID.randomUUID().toString().substring(0, 8);

    // Locators (adjust these according to the actual Koel application)
    private final By emailField = By.cssSelector("input[type='email']");
    private final By passwordField = By.cssSelector("input[type='password']");
    private final By loginButton = By.cssSelector("button[type='submit']");
    private final By newPlaylistButton = By.xpath("//button[contains(text(),'New Playlist') or contains(@title,'New Playlist')]");
    private final By playlistNameInput = By.xpath("//input[@placeholder='Playlist name']");
    private final By createButton = By.xpath("//button[contains(text(),'Create')]");
    private final By deletePlaylistButton = By.xpath("//button[contains(@class,'btn-delete-playlist') or contains(text(),'Delete')]");

    private By getPlaylistLink(String name) {
        return By.xpath("//a[contains(text(),'" + name + "')]");
    }

    private void login(String email, String password) {
        waitForElementVisible(emailField).sendKeys(email);
        waitForElementVisible(passwordField).sendKeys(password);
        waitForElementClickable(loginButton).click();

        // Wait for the home page to load (URL contains "#!/home")
        wait.until(ExpectedConditions.urlContains("#!/home"));
    }

    private boolean playlistExists(String name) {
        try {
            return driver.findElements(getPlaylistLink(name)).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private void createPlaylist(String name) {
        waitForElementClickable(newPlaylistButton).click();
        waitForElementVisible(playlistNameInput).sendKeys(name);
        waitForElementClickable(createButton).click();

        // Wait for the new playlist to appear in the sidebar
        waitForElementVisible(getPlaylistLink(name));
    }

    private void clickPlaylist(String name) {
        waitForElementClickable(getPlaylistLink(name)).click();

        // Wait for the playlist page to load (URL contains "#!/playlist/")
        wait.until(ExpectedConditions.urlContains("#!/playlist/"));
    }

    private void deleteCurrentPlaylist() {
        waitForElementClickable(deletePlaylistButton).click();

        // Handle confirmation dialog if present (some apps require confirmation)
        try {
            By confirmButton = By.xpath("//button[contains(text(),'Delete') or contains(text(),'Confirm')]");
            waitForElementClickable(confirmButton).click();
        } catch (Exception e) {
            // No confirmation dialog – proceed
        }
    }

    @Test
    @Parameters("baseUrl")
    public void deletePlaylist(String baseUrl) {
        // 1. Login (the URL is already opened by BaseTest.setUp)
        login("your_email@example.com", "your_password");

        // 2. Check if playlist exists; if not, create it
        if (!playlistExists(playlistName)) {
            createPlaylist(playlistName);
        }

        // 3. Click on the playlist
        clickPlaylist(playlistName);

        // 4. Delete the playlist
        deleteCurrentPlaylist();

        // 5. Get the notification message
        String actualMessage = waitForNotificationAndGetText();
        String expectedMessage = "Deleted playlist " + playlistName;

        // 6. Assert
        Assert.assertEquals(actualMessage, expectedMessage,
                "Notification message does not match expected text.");
    }
}
