import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.UUID;

public class Homework21 extends BaseTest {

    private final String originalPlaylistName = "Playlist_" + UUID.randomUUID().toString().substring(0, 8);
    private final String newPlaylistName = "Renamed_" + UUID.randomUUID().toString().substring(0, 8);

    // Locators (update according to the actual Koel application)
    private final By emailField = By.cssSelector("input[type='email']");
    private final By passwordField = By.cssSelector("input[type='password']");
    private final By loginButton = By.cssSelector("button[type='submit']");
    private final By newPlaylistButton = By.xpath("//button[contains(text(),'New Playlist') or contains(@title,'New Playlist')]");
    private final By playlistNameInput = By.xpath("//input[@placeholder='Playlist name']");
    private final By createButton = By.xpath("//button[contains(text(),'Create')]");

    // Dynamic locator for a playlist link by its name
    private By getPlaylistLink(String name) {
        return By.xpath("//a[contains(text(),'" + name + "')]");
    }

    // Locator for the inline rename input field (appears after double‑click)
    private final By renameInput = By.xpath("//input[@class='playlist' or contains(@placeholder,'Playlist name')]");

    private void login(String email, String password) {
        waitForElementVisible(emailField).sendKeys(email);
        waitForElementVisible(passwordField).sendKeys(password);
        waitForElementClickable(loginButton).click();
        wait.until(ExpectedConditions.urlContains("#!/home"));
    }

    private void createPlaylist(String name) {
        waitForElementClickable(newPlaylistButton).click();
        waitForElementVisible(playlistNameInput).sendKeys(name);
        waitForElementClickable(createButton).click();
        // Wait for the playlist to appear in the sidebar
        waitForElementVisible(getPlaylistLink(name));
    }

    // ---- Action Class method: double-click to rename ----
    private void renamePlaylistViaDoubleClick(String oldName, String newName) {
        WebElement playlistLink = waitForElementVisible(getPlaylistLink(oldName));

        // 1. Double‑click the playlist name using Actions class
        Actions actions = new Actions(driver);
        actions.doubleClick(playlistLink).perform();

        // 2. Wait for the inline rename input field to be visible
        WebElement renameField = waitForElementVisible(renameInput);

        // 3. Clear the existing name and type the new name
        renameField.clear();
        renameField.sendKeys(newName);

        // 4. Press ENTER to save the new name
        renameField.sendKeys(Keys.ENTER);

        // 5. Wait for the playlist link to be updated with the new name
        waitForElementVisible(getPlaylistLink(newName));
    }

    // ---- Get the notification text after renaming ----
    private String getNotificationText() {
        By notificationLocator = By.xpath("//div[contains(@class,'notification') or contains(@class,'toast') or contains(@class,'alert')]");
        WebElement notification = waitForElementVisible(notificationLocator);
        return notification.getText();
    }

    @Test
    @Parameters("baseUrl")
    public void renamePlaylist(String baseUrl) {
        // 1. Login (the URL is already opened by BaseTest.setUp)
        login("your_email@example.com", "your_password");

        // 2. Create a new playlist (so we always have one to rename)
        createPlaylist(originalPlaylistName);

        // 3. Rename the playlist using double-click (Actions class)
        renamePlaylistViaDoubleClick(originalPlaylistName, newPlaylistName);

        // 4. Get the notification message
        String actualMessage = getNotificationText();
        String expectedMessage = "Updated playlist " + newPlaylistName;  // adjust text as per Koel

        // 5. Assert that the notification confirms the rename
        Assert.assertEquals(actualMessage, expectedMessage,
                "Rename notification does not match expected text.");
    }
}
