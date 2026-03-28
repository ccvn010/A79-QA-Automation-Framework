import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTests extends BaseTest {

    @Test
    public void loginEmptyEmailPassword() {
        // Use getDriver() from BaseTest – this driver is already connected to LambdaTest cloud
        String url = "https://qa.koel.app/";
        getDriver().get(url);
        Assert.assertEquals(getDriver().getCurrentUrl(), url);
    }
}
