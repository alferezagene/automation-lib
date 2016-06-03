package AutomationLibrary;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ElementList {
    public List<WebElement> elementList = null;
    public By byLocator = null;

    //used to instantiate a new webelement list but doesn't fetch till accessed, as per Element
    public ElementList(By by) {
        this.byLocator = by;
    }

    //used to pass in an existing list retrieved from other elements
    public ElementList(List<WebElement> existingList) {
        this.elementList = existingList;
    }

    private List<WebElement> getElementList() {
        if (elementList != null) {
            return elementList;
        } else {
            return Automator.driver.findElements(byLocator);
        }
    }

    public void clickOnItemByText(String textToClick) {
        for (WebElement listItem : getElementList()) {
            String text = listItem.findElement(By.xpath("")).getText();
            if (text.toLowerCase().equals(textToClick.toLowerCase())) {
                listItem.click();
                break;
            }
        }
    }

    public int size() {
        if (elementList != null) {
            return elementList.size();
        } else {
            return getElementList().size();
        }
    }

    public WebElement get(int position) {
        if (elementList != null) {
           return elementList.get(position);
        } else {
            return getElementList().get(position);
        }
    }
}
