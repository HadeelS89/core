package com.qpros.helpers;

import com.qpros.common.Base;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ActionsHelper extends Base {
    protected static Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
    public static WebDriverWait wait;

    public static void waitForSeconds(Integer timeWait) {
        driver.manage().timeouts().implicitlyWait(timeWait, TimeUnit.SECONDS);
    }

    public static boolean waitVisibility(WebElement element, int time) {
        boolean isElementPresent = false;
        try {
            wait = new WebDriverWait(driver, time);
            wait.until(ExpectedConditions.visibilityOf(element));
            isElementPresent = element.isDisplayed();
        } catch (Exception e) {
            throw e;
        }
        return isElementPresent;

    }

    public static boolean waitToBeClickable(WebElement element, int time) {
        boolean isElementClickable;
        try {
            wait = new WebDriverWait(driver, time);
            wait.until(ExpectedConditions.elementToBeClickable(element));
            isElementClickable = element.isEnabled();
        } catch (Exception e) {
            throw e;
        }
        return isElementClickable;

    }

    public static void selectByIndex(WebElement element, int index) {
        try {
            org.openqa.selenium.support.ui.Select make = new org.openqa.selenium.support.ui.Select(element);
            make.selectByIndex(index);
        } catch (Exception e) {
            throw new RuntimeException("Element is: " + element);

        }
    }

    public static void selectByValue(WebElement element, String value) {
        try {
            org.openqa.selenium.support.ui.Select make = new org.openqa.selenium.support.ui.Select(element);
            make.selectByValue(value);
        } catch (Exception e) {
            throw new RuntimeException("Element is: " + element);

        }
    }

    public static void scrollTo(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", element);
    }

    public static void safeJavaScriptClick(WebElement element) throws Exception {
        try {
            if (element.isEnabled() && element.isDisplayed()) {

                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            } else {
                LOGGER.info("Unable to click on element");
            }
        } catch (StaleElementReferenceException e) {
            LOGGER.info("Element is not attached to the page document " + e.getStackTrace());
        } catch (NoSuchElementException e) {
            LOGGER.info("Element was not found in DOM " + e.getStackTrace());
        } catch (Exception e) {
            LOGGER.info("Unable to click on element " + e.getStackTrace());
        }
    }

    public static String getImagePath(String imageName) {
        String path = System.getProperty("user.dir") + "/src/main/resources/images/" + imageName;
        return path;
    }

    public String getXMLPath(String xmlFileName) {
        String path = System.getProperty("user.dir") + "/src/main/resources/xmlfiles/" + xmlFileName;
        return path;
    }

    public static String getTodayDate() {
        LocalDate localDate = LocalDate.now();
        return localDate.toString();
    }


    public static Calendar getTodayDateFromCalender() {
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        return cal;
    }

    public static WebElement getElement(WebDriver driver, String attribute, String locator) {

        WebElement element = null;

        try {
            switch (attribute) {
                case "id":
                    element = driver.findElement(By.id(locator));
                case "name":
                    element = driver.findElement(By.id(locator));
                case "class":
                    element = driver.findElement(By.className(locator));
                case "xpath":
                    element = driver.findElement(By.xpath(locator));
            }
        } catch (Exception e) {
        }

        return element;
    }

    public static void actionsClick(WebElement element, String EnterText) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element);
        actions.click();
        actions.sendKeys(EnterText, Keys.ENTER);
        actions.build().perform();

    }
    public static void click(WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element);
        actions.click();
        actions.build().perform();

    }
    public static String getFutureDate(int addedYears, int addedMonths, int addedDays) {
        DateFormat dateFormat;
        if (ReadWriteHelper.ReadData("browser").equalsIgnoreCase("chrome")) {
            dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        } else {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }


        Date currentDate = new Date();
        //System.out.println(dateFormat.format(currentDate));

        // convert date to calendar
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);

        // manipulate date
        c.add(Calendar.YEAR, addedYears);
        c.add(Calendar.MONTH, addedMonths);
        c.add(Calendar.DATE, addedDays); //same with c.add(Calendar.DAY_OF_MONTH, 1);

        // convert calendar to date
        Date currentDatePlus = c.getTime();

        //System.out.println(dateFormat.format(currentDatePlus));

        return dateFormat.format(currentDatePlus);
    }


    public static void selectElementFromList(List<WebElement> element, String value) {
        element.parallelStream().forEach(element1 -> {
            if (element1.getText().equalsIgnoreCase(value)) {
                element1.click();
            }
        });
    }

    public static boolean waitForExistance(WebElement element, int seconds) {
        boolean isExist = false;

        int count = 1;
        while (count <= seconds) {
            try {
                Thread.sleep(1000);
                if (element.isDisplayed()) {
                    isExist = true;
                    break;
                }
            } catch (Exception e) {
                System.out.println("Exception message: " + e.getMessage());
            }
            count++;
        }

        return isExist;
    }

    public static boolean waitForListExistance(List<WebElement> element, int seconds) {
        boolean isExist = false;
        int count = 1;
        while (count <= seconds) {
            try {
                Thread.sleep(1000);
                if (element.size() != 0 || element.get(count).isDisplayed() && element.get(count).isEnabled()) {
                    isExist = true;
                    break;
                }
            } catch (Exception e) {
                System.out.println("Exception message: " + e.getMessage());
            }
            count++;
        }
        return isExist;
    }

    public static String reverseString(String value) {

        StringBuilder reverse = new StringBuilder();

        for (int i = value.length() - 1; i >= 0; i--) {
            reverse.append(value.charAt(i));
        }

        return reverse.toString();
    }

    public static WebElement getElementFromList(List<WebElement> element, String value) {
        WebElement elmnt = null;
        for (int i = 0; i < element.size(); i++) {
            if (element.get(i).getText().equalsIgnoreCase(value)) {
                elmnt = element.get(i);
                break;
            }
        }
        return elmnt;
    }

    public static void sendText(String locator, String value) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.getElementById('" + locator + "').value='" + value + "';");

    }

    public static void HandleKendoDateTimePicker(String day, String month, String year) throws InterruptedException {
         WebElement select = driver.findElement(By.cssSelector("table .picker-switch"));
         select.click();
         select= driver.findElement(By.cssSelector(".datepicker-months .picker-switch"));
         select.click();
         select= driver.findElement(By.xpath(String.format("//span[contains(.,'%s')]",year)));
         select.click();
         select= driver.findElement(By.xpath(String.format("//span[contains(.,'%s')]",month)));
         select.click();
         select=driver.findElement(By.xpath(String.format("//td[contains(.,'%s')]",day)));
         select.click();
         Thread.sleep(2000);
     }

    public static void navigateTo(String url){
        driver.navigate().to(url);
    }

    public static String getFutureTime(int addedHours, int addedMins){
        DateFormat dateFormat = new SimpleDateFormat("hh-mmaa");
        Date currentDate = new Date();
        // convert date to calendar
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add( Calendar.HOUR, addedHours );
        c.add( Calendar.MINUTE, addedMins );
        // convert calendar to date
        Date currentDatePlus = c.getTime();
        return dateFormat.format(currentDatePlus);
    }

    public static void retryClick(WebElement myelement, int maxSeconds)  {
        int i = 0;
        boolean result = false;
        while (i <= maxSeconds){
            try {
                myelement.click();
                result = true;
                break;
            } catch (Exception e) {
                result = false;
            }
            i++;
            try {
                Thread.sleep(1000);
            }catch (Exception e){

            }
        }
        if(!result){
            Assert.fail("Failed to click element: " + myelement.toString());
        }



    }


}
