package AutomationLibTests;


import nz.co.thebteam.AutomationLibrary.AutomationType;
import nz.co.thebteam.AutomationLibrary.Automator;
import nz.co.thebteam.AutomationLibrary.Element;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import static org.junit.Assert.assertEquals;


public class AndroidTest {

    private Automator automator;
    @After
    public void quit(){
        automator.quit();
    }

    @Test @Ignore
    public void TestAndroidBrowser(){
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "android");
        caps.setCapability("deviceName", "android");
        caps.setCapability("autoAcceptAlerts", true);
        caps.setBrowserName("Browser");
         automator = new Automator(AutomationType.ANDROIDBROWSER, caps);
        Automator.driver.navigate().to("http://www.lightbox.co.nz");
        Element signUpButton = new Element(By.cssSelector("body > div > div > mobile-nav > div > div > header > nav > div > a.btn.btn-primary"));
        signUpButton.waitForElementToBeDisplayed(10);
        assertEquals("Sign up", signUpButton.getText());
    }
}
