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

public class Homework18 {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        // Set ChromeDriver path (update for your system)
        System.setProperty("webdriver.chrome.driver", "/path/to/your/chromedriver");
        
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
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
        // Wait for main content to load
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[contains(@class, 'main-view')]")));
    }

    // Helper method: Play next song and then play
    private void playNextThenPlay() {
        // Click "Play next song" button
        WebElement playNextButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(@title, 'Play next') or contains(@aria-label, 'next')]")));
        playNextButton.click();
        
        // Small delay to ensure next song is loaded
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Click Play button
        WebElement playButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(@title, 'Play') and not(contains(@title, 'Pause'))]")));
        playButton.click();
    }

    // Helper method: Validate song is playing
    private boolean validateSongIsPlaying() {
        try {
            // Check for pause button (appears when song is playing)
            WebElement pauseButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//button[contains(@title, 'Pause')]")));
            
            // Also check for sound bar/visualizer (common in music players)
            WebElement soundBar = driver.findElement(
                By.xpath("//*[contains(@class, 'sound-bar') or contains(@class, 'visualizer') or contains(@class, 'wave')]"));
            
            return pauseButton.isDisplayed() || soundBar.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Test
    public void playSong() {
        // 1. Login with your credentials
        login("your_email@example.com", "your_password");
        
        // 2. Click "Play next song" then Play button
        playNextThenPlay();
        
        // 3. Validate that a song is playing
        boolean isPlaying = validateSongIsPlaying();
        
        // 4. Assert that song is playing
        Assert.assertTrue(isPlaying, "Song should be playing but no pause button or sound bar found");
        
        System.out.println("Test passed: Song is playing successfully!");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            // Small delay to observe the playing state before closing
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driver.quit();
        }
    }
}
