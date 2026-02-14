import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import java.time.Duration;
import java.util.UUID;

public class Homework19 {
    WebDriver driver;
    WebDriverWait wait;
    String playlistName = "TestPlaylist_" + UUID.randomUUID().toString().substring(0, 8);

    @BeforeMethod
    @Parameters("baseUrl")
    public void setUp(String baseUrl) {
        // Set ChromeDriver path (update for your system)
        System.setProperty("webdriver.chrome.driver", "/path/to/your/chromedriver");
        
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        // Navigate to baseUrl from TestNG XML
        driver.get(baseUrl);
    }

    // Helper method: Login
    private void login(String email, String password) {
        WebElement emailField = driver.findElement(By.cssSelector("input[type='email']"));
        WebElement passwordField = driver.findElement(By.cssSelector("input[type='password']"));
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        
        emailField.sendKeys(email);
        passwordField.sendKeys(password);
        loginButton.click();
        
        // Wait for login to complete
        wait.until(ExpectedConditions.urlContains("#!/home"));
        // Wait for main content to load
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[contains(@class, 'main-view')]")));
    }

    // Helper method: Check if playlist exists
    private boolean playlistExists(String playlistName) {
        try {
            WebElement playlist = driver.findElement(
                By.xpath("//a[contains(text(), '" + playlistName + "')]"));
            return playlist.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Helper method: Click on playlist
    private void clickPlaylist(String playlistName) {
        WebElement playlist = driver.findElement(
            By.xpath("//a[contains(text(), '" + playlistName + "')]"));
        playlist.click();
        
        // Wait for playlist page to load
        wait.until(ExpectedConditions.urlContains("#!/playlist/"));
    }

    // Helper method: Create new playlist
    private void createPlaylist(String playlistName) {
        WebElement newPlaylistButton = driver.findElement(
            By.xpath("//button[contains(text(), 'New Playlist') or contains(@title, 'New Playlist')]"));
        newPlaylistButton.click();
        
        WebElement playlistNameInput = driver.findElement(
            By.xpath("//input[@placeholder='Playlist name']"));
        playlistNameInput.sendKeys(playlistName);
        
        WebElement createButton = driver.findElement(
            By.xpath("//button[contains(text(), 'Create')]"));
        createButton.click();
        
        // Wait for playlist to appear
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//a[contains(text(), '" + playlistName + "')]")));
    }

    // Helper method: Delete playlist
    private void deletePlaylist(String playlistName) {
        // Click the red "x PLAYLIST" button
        WebElement deleteButton = driver.findElement(
            By.xpath("//button[contains(@class, 'btn-delete-playlist') or contains(text(), 'Delete')]"));
        deleteButton.click();
        
        // Wait for confirmation if needed (some apps have confirmation dialog)
        try {
            WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Delete') or contains(text(), 'Confirm')]")));
            confirmButton.click();
        } catch (Exception e) {
            // No confirmation dialog, proceed
        }
    }

    // Helper method: Get notification text
    private String getNotificationText() {
        WebElement notification = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[contains(@class, 'notification') or contains(@class, 'toast') or contains(@class, 'alert')]")));
        return notification.getText();
    }

    @Test
    @Parameters("baseUrl")
    public void deletePlaylist(String baseUrl) {
        // 1. Login with your credentials (use your actual credentials)
        login("your_email@example.com", "your_password");
        
        // 2. Check if playlist exists
        if (!playlistExists(playlistName)) {
            // Create new playlist if it doesn't exist
            createPlaylist(playlistName);
        }
        
        // 3. Click on the playlist
        clickPlaylist(playlistName);
        
        // 4. Click the red "x PLAYLIST" button to delete
        deletePlaylist(playlistName);
        
        // 5. Get notification text
        String actualNotification = getNotificationText();
        String expectedNotification = "Deleted playlist " + playlistName;
        
        // 6. Verify notification using Assert.assertEquals()
        Assert.assertEquals(actualNotification, expectedNotification, 
            "Notification message doesn't match expected text");
        
        System.out.println("Test passed: Playlist deleted successfully!");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            try {
                Thread.sleep(2000); // Wait to see the result
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driver.quit();
        }
    }
}
