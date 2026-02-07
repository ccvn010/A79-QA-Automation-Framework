import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.time.Duration;
import java.util.UUID;

public class Homework17 {
    WebDriver driver;
    WebDriverWait wait;
    String uniquePlaylistName;

    @BeforeMethod
    public void setUp() {
        // Set ChromeDriver path (update for your system)
        System.setProperty("webdriver.chrome.driver", "/path/to/your/chromedriver");
        
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        // Generate unique playlist name
        uniquePlaylistName = "MyPlaylist_" + UUID.randomUUID().toString().substring(0, 8);
    }

    // Helper method: Login
    private void login(String email, String password) {
        driver.get("https://qa.koel.app/");
        
        WebElement emailField = driver.findElement(By.cssSelector("input[type='email']"));
        WebElement passwordField = driver.findElement(By.cssSelector("input[type='password']"));
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        
        emailField.sendKeys(email);
        passwordField.sendKeys(password);
        loginButton.click();
        
        // Wait for login to complete
        wait.until(ExpectedConditions.urlContains("#!/home"));
    }

    // Helper method: Search for a song
    private void searchSong(String songName) {
        WebElement searchBox = driver.findElement(By.cssSelector("input[type='search']"));
        searchBox.clear();
        searchBox.sendKeys(songName);
        
        // Wait for search results
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[contains(@class, 'song-item')]")));
    }

    // Helper method: Click View All button
    private void clickViewAll() {
        WebElement viewAllButton = driver.findElement(
            By.xpath("//button[contains(text(), 'View All') or contains(., 'view all')]"));
        viewAllButton.click();
    }

    // Helper method: Select first song
    private void selectFirstSong() {
        WebElement firstSong = driver.findElement(
            By.xpath("(//div[contains(@class, 'song-item')]//button)[1]"));
        firstSong.click();
    }

    // Helper method: Click ADD TO button
    private void clickAddToButton() {
        WebElement addToButton = driver.findElement(
            By.xpath("//button[contains(text(), 'ADD TO')]"));
        addToButton.click();
        
        // Wait for playlist modal to appear
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[contains(@class, 'playlist-modal')]")));
    }

    // Helper method: Create and select playlist
    private void createAndSelectPlaylist(String playlistName) {
        // Look for existing playlist or create new
        WebElement newPlaylistInput = driver.findElement(
            By.xpath("//input[@placeholder='New Playlist']"));
        newPlaylistInput.sendKeys(playlistName);
        
        WebElement createButton = driver.findElement(
            By.xpath("//button[contains(text(), 'Create')]"));
        createButton.click();
        
        // Wait for playlist to be selected
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
            By.xpath("//div[contains(@class, 'playlist-modal')]")));
    }

    // Helper method: Get notification message
    private String getNotificationMessage() {
        WebElement notification = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[contains(@class, 'notification') or contains(@class, 'toast')]")));
        return notification.getText();
    }

    @Test
    public void addSongToPlaylist() {
        // 1. Login with your credentials
        login("your_email@example.com", "your_password");
        
        // 2. Search for a song
        searchSong("Blinding Lights"); // Replace with any song
        
        // 3. Click 'View All' button
        clickViewAll();
        
        // 4. Click the first song in search results
        selectFirstSong();
        
        // 5. Click 'ADD TO...' button
        clickAddToButton();
        
        // 6. Create and select playlist with unique name
        createAndSelectPlaylist(uniquePlaylistName);
        
        // 7. Get notification message
        String actualMessage = getNotificationMessage();
        String expectedMessage = "Added 1 song into " + uniquePlaylistName;
        
        // 8. Verify notification using Assert.assertEquals()
        Assert.assertEquals(actualMessage, expectedMessage, 
            "Notification message doesn't match expected text");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
