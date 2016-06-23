package AutomationLibTests;


import nz.co.thebteam.AutomationLibrary.AutomationType;
import nz.co.thebteam.AutomationLibrary.Automator;
import nz.co.thebteam.AutomationLibrary.Element;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;

import static org.junit.Assert.assertEquals;

public class WebTest {
    private Automator automator;
    @After
    public void quit(){
        automator.quit();
    }

    @Test
    public void TestChrome(){
         automator = new Automator(AutomationType.CHROME);
        Automator.driver.navigate().to("http://www.lightbox.co.nz");
        Element signUpButton = new Element(By.xpath("//button[contains(@label, 'SIGN UP')]"));
        signUpButton.waitForElementToBeDisplayed(10);
        assertEquals("SIGN UP", signUpButton.getText());
       }

    @Test @Ignore
    public void TestFirefox(){
         automator = new Automator(AutomationType.FIREFOX);
        Automator.driver.navigate().to("http://www.lightbox.co.nz");
        Element signUpButton = new Element(By.xpath("//button[contains(@label, 'SIGN UP')]"));
        signUpButton.waitForElementToBeDisplayed(10);
        assertEquals("SIGN UP", signUpButton.getText());
    }

    @Test @Ignore
    public void TestIE(){
         automator = new Automator(AutomationType.IE);
        Automator.driver.navigate().to("http://www.lightbox.co.nz");
        Element signUpButton = new Element(By.xpath("//button[contains(@label, 'SIGN UP')]"));
        signUpButton.waitForElementToBeDisplayed(10);
        assertEquals("SIGN UP", signUpButton.getText());
    }

    @Test @Ignore //has problems with admin account
    public void TestEdge(){
         automator = new Automator(AutomationType.EDGE);
        Automator.driver.navigate().to("http://www.lightbox.co.nz");
        Element signUpButton = new Element(By.xpath("//button[contains(@label, 'SIGN UP')]"));
        signUpButton.waitForElementToBeDisplayed(10);
        assertEquals("SIGN UP", signUpButton.getText());
    }
}
