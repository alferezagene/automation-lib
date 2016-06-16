package nz.co.thebteam.AutomationLibrary;


import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.HashMap;

import static org.junit.Assert.assertFalse;

public class Element {
    public By locator = null;
    public static WebElement webElement = null;


    //when instantiating the element we DON'T fetch it. This is so we can set them up without them being visible yet.
    public Element(By locator) {
        this.locator = locator;
    }

    public Element(WebElement element) {
        webElement = element;
    }

    //Waits x seconds for the element to exist - note does not check visibility
    public void waitForElementToExist(int seconds) {
        int i = 0;
        while (i < seconds) {
            if (exists()) {
                return;
            } else {
                HelperMethods.sleep(1);
                i++;
            }
        }
        try {
            throw new Exception("Element does not exist after " + seconds + " seconds.");
        } catch (Exception e) {
            e.printStackTrace();
            assertFalse(false);
        }
    }

    //Waits x seconds for the element to be displayed
    public void waitForElementToBeDisplayed(int seconds) {
        int i = 0;
        while (i < seconds) {
            if (isElementDisplayed(this.locator)) {
                return;
            } else {
                HelperMethods.sleep(1);
                i++;
            }
        }
        try {
            throw new AssertionError("Element is not displayed after " + seconds + " seconds.");
        } catch (Exception e) {
            e.printStackTrace();
            assert (false);

        }
    }

    //Waits x seconds for the element's attribute to be the specified value
    public void waitForElementAttribute(int seconds, String attribute, String value) {
        int i = 0;
        while (i < seconds) {
            if (isElementDisplayed(this.locator)) {
              if (findElement().getAttribute(attribute).toLowerCase().equals(value.toLowerCase()))
                return;
            } else {
                HelperMethods.sleep(1);
                i++;
            }
        }
        try {
            throw new AssertionError("Element's attribute "+attribute+" was not "+value+" after " + seconds + " seconds.");
        } catch (Exception e) {
            e.printStackTrace();
            assert (false);

        }
    }

    //Checks for existence
    public boolean exists() {
        if (Automator.driver.findElements(this.locator).size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    //Checks for visibility
    public boolean isElementDisplayed(By locator) {
        if (Automator.driver.findElements(locator).size() > 0) {
            if (Automator.driver.findElement(locator).isDisplayed()) {
                return true;
            }
        }
        return false;
    }

    //retrieves the webelement if it exists, otherwise return null
    public WebElement findElement() {
       if (locator == null) {
           return webElement;
       } else {
           if (Automator.driver.findElements(locator).size() > 0) {
               return Automator.driver.findElement(locator);
           } else {
               try {
                   throw new Exception("Element does not exist.");
               } catch (Exception e) {
                   e.printStackTrace();
                   assert (false);
               }
               return null;
           }
       }
    }

    //wrapper everything here.
    //Using find element each time means we always get a fresh copy, it stops the nasty webelement stale reference errors.
    //This is the major guts of a webdriver library, handling the element and passing commands through

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                          Basic commands                                                        //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void click() {
        findElement().click();
    }

    public String getText() {
        return findElement().getText();
    }

    public void sendKeys(String keys) {
        findElement().sendKeys(keys);
    }

    public Boolean isDisplayed() {
      try {
          return findElement().isDisplayed();
      } catch (Exception e) {
          return false;
      }
    }

    public Boolean isEnabled() {
        try {
            return findElement().isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean isSelected() {
        try {
            return findElement().isSelected();
        } catch (Exception e) {
            return false;
        }
    }

    //used for IOS as it pastes in the value rather than tapping each key
    public void setValue(String string) {
        if ((Automator.driver.getClass().toString().toLowerCase().contains("iosdriver"))) {
            IOSElement element = (IOSElement) findElement();
            element.setValue(string);
        } else {
            findElement().sendKeys(string);
        }
    }

    public void clear() {
        findElement().clear();
    }

    public String getAttribute(String attributeName) {
        return findElement().getAttribute(attributeName);
    }

    public Point getLocation() {
        return findElement().getLocation();
    }

    public ElementList findSubElements(By by) {
        return new ElementList(findElement().findElements(by));
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                            Swipes/Scrolls/Touches                                                  //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void dismissKeyboard() {
        Automator.driver.navigate().back();
    }

    public void pressAndroidKeyCode(int keyCode) {
        AndroidDriver driver = (AndroidDriver) Automator.driver;
        driver.pressKeyCode(keyCode);
    }

    public void scrollToElement() {
        Point hoverItem = findElement().getLocation();
        ((JavascriptExecutor) Automator.driver).executeScript("window.scrollBy(0," + (hoverItem.getY() - 200) + ");");
    }

    public void scrollToElement(int offset) {
        Point hoverItem = findElement().getLocation();
        ((JavascriptExecutor) Automator.driver).executeScript("window.scrollBy(0," + (hoverItem.getY() - offset) + ");");
    }

    public void flickDown() {
        JavascriptExecutor js = (JavascriptExecutor) Automator.driver;
        HashMap<String, Double> flickObject = new HashMap<>();
        flickObject.put("endX", 0.0);
        flickObject.put("endY", 100.0);
        flickObject.put("touchCount", 1.0);
        js.executeScript("mobile: flick", flickObject);
    }

    public void hoverOver() {
        WebElement element = findElement();
        String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover',true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
        ((JavascriptExecutor) Automator.driver).executeScript(mouseOverScript, element);
    }

    public void hoverOut() {
        if (Automator.driver.getClass().toString().toLowerCase().contains("safari")) {
            WebElement element = Automator.driver.findElement(By.xpath("//title"));
            String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover',true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
            ((JavascriptExecutor) Automator.driver).executeScript(mouseOverScript, element);
        } else {
            WebElement element = Automator.driver.findElement(By.xpath("//title"));
            Actions actions = new Actions(Automator.driver);
            Actions moveto = actions.moveToElement(element);
            moveto.perform();

        }

    }
}
