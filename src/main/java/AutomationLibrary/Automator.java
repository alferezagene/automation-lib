package AutomationLibrary;


import cucumber.api.Scenario;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.EdgeDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Automator {
    public static WebDriver driver;
    public DesiredCapabilities caps = null;
    public String URL = null;
    public String appiumURL = "http://127.0.0.1:4723/wd/hub"; //windows
    public String automationPlatform = null;

    //uses default settings
    public Automator(AutomationType automationType) {
        setupDriver(automationType);
    }

    //Specify your own caps to use - all emulators/devices really should do this and not use the defaults
    public Automator(AutomationType automationType, DesiredCapabilities caps) {
        this.caps = caps;
        setupDriver(automationType);
    }

    //Non-standard remote driver instantiation eg Saucelabs connection, non-standard appium
    public Automator(DesiredCapabilities caps, String URL) {
        this.caps = caps;
        this.URL = URL;
        setupRemoteWebDriver();
    }


    //switch for basic setup
    private void setupDriver(AutomationType automationType) {

        switch (automationType) {
            case FIREFOX:
                setupFirefox();
                automationPlatform = "web";
                break;
            case CHROME:
                setupChrome();
                automationPlatform = "web";
                break;
            case IE:
                setupIE();
                automationPlatform = "web";
                break;
            case EDGE:
                setupEdge();
                automationPlatform = "web";
                break;
            case SAFARI:
                setupSafari();
                automationPlatform = "web";
                break;
            case ANDROID:
                setupAndroid();
                automationPlatform = "mobileAndroid";
                break;
            case ANDROIDBROWSER:
                setupAndroidBrowser();
                automationPlatform = "mobileAndroid";
                break;
            case IOS:
                setupIOS();
                automationPlatform = "mobileIos";
                break;
            case IOSBROWSER:
                setupIOSBrowser();
                automationPlatform = "mobileIos";
                break;
            default:

        }
    }

    private void setupFirefox() {
        if (caps == null) {
            caps = new DesiredCapabilities().firefox();
        }
        driver = new FirefoxDriver((caps));
    }

    private void setupChrome() {
        ChromeDriverManager.getInstance().setup();
        if (caps == null) {
            caps = new DesiredCapabilities().chrome();
        }
        driver = new ChromeDriver(caps);
    }

    private void setupIE() {
        InternetExplorerDriverManager.getInstance().setup();
        if (caps == null) {
            caps = new DesiredCapabilities().internetExplorer();
        }
        driver = new InternetExplorerDriver((caps));
    }

    private void setupSafari() {
        if (caps == null) {
            caps = new DesiredCapabilities().safari();
        }
        driver = new SafariDriver((caps));
    }

    private void setupAndroid() {
        if (caps == null) {
            caps = new DesiredCapabilities().android();
        }
        this.URL = appiumURL;
        setupRemoteWebDriver();
    }

    private void setupAndroidBrowser() {
        if (caps == null) {
            caps = new DesiredCapabilities().android();
        }
        this.URL = appiumURL;
        setupRemoteWebDriver();
    }

    private void setupIOS() {
        if (caps == null) {
            caps = new DesiredCapabilities();
            caps.setCapability("platformName", "iOS");
        }
        this.URL = appiumURL;
        setupRemoteWebDriver();
    }

    private void setupIOSBrowser() {
        if (caps == null) {
            caps = new DesiredCapabilities();
            caps.setCapability("platformName", "iOS");
        }
        this.URL = appiumURL;
        setupRemoteWebDriver();
    }

    private void setupEdge() {
        EdgeDriverManager.getInstance().setup();
        if (caps == null) {
            caps = new DesiredCapabilities().edge();
        }
        driver = new EdgeDriver((caps));
    }

    private void setupRemoteWebDriver() {
        //TODO - saucelabs file upload
        try {
            driver = new RemoteWebDriver(new URL(this.URL), caps);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void quit() {
        driver.quit();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                        Webdriver-level stuff                                                   //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void takeScreenshot(Scenario scenario) {
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        scenario.embed(screenshot, "image/png");
    }


    public void switchToDefaultTab() {
        List<String> browserTabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(browserTabs.get(0));
    }

    public void switchToTab(int index) {
        List<String> browserTabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(browserTabs.get(index));
    }

    public void closeTab(int index) {
        List<String> browserTabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(browserTabs.get(index));
        driver.close();
        switchToDefaultTab();
    }

    public String getCurrentPageUrl() {
        return driver.getCurrentUrl();
    }

    public void scrollToBottomOfPage() {
        JavascriptExecutor js = ((JavascriptExecutor) driver);
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    public void scrollToTopOfPage() {
        JavascriptExecutor js = ((JavascriptExecutor) driver);
        js.executeScript("window.scrollTo(0, 0)");
    }

    public void scrollTo(WebElement element) {
        if (automationPlatform.equals("web")) {
            Point hoverItem = element.getLocation();
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0," + (hoverItem.getY() - 400) + ");");
        } else if (automationPlatform.contains("mobile")) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        }
    }

    public void switchToDefaultFrame() {
        driver.switchTo().defaultContent();
    }

    public void switchToFrame(By by) {
        try {
            driver.switchTo().frame(driver.findElement(by));
        } catch (Exception e) {
            //already on it;
        }
    }

}
