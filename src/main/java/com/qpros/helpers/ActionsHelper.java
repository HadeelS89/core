package com.qpros.helpers;

import com.qpros.common.Base;
import com.qpros.common.Constant;
import com.qpros.common.LogManger;
import com.qpros.exceptions.InvalidElementTypeException;
import com.qpros.exceptions.TooFewWindowsException;
import com.qpros.exceptions.TooManyWindowsException;
import com.qpros.exceptions.UnableToFindWindowException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class ActionsHelper extends Base {
    protected static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
    public static WebDriverWait wait;
    public static final LogManger logManger= new LogManger(ActionsHelper.class.getSimpleName());

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
        return System.getProperty("user.dir") + "/src/main/resources/images/" + imageName;
    }

    public String getXMLPath(String xmlFileName) {
        return System.getProperty("user.dir") + "/src/main/resources/xmlfiles/" + xmlFileName;
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
        for (WebElement webElement : element) {
            if (webElement.getText().equalsIgnoreCase(value)) {
                elmnt = webElement;
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

    /**
     * @param myelement WebElement to click
     * @param maxSeconds When to Timeout
     */
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

    public static WebElement waitForElementToBeClickable(WebElement element, String name) throws Exception {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Constant.WaitingSeconds);
            wait.until(ExpectedConditions.elementToBeClickable(element));
            logManger.INFO("Wait for element '" + name + "' is performed");
            return element;
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static WebElement waitForElementToBeClickable(WebElement element, String name, int waitSeconds) throws Exception {
        try {
            WebDriverWait wait = new WebDriverWait(driver, waitSeconds);
            wait.until(ExpectedConditions.elementToBeClickable(element));
            logManger.INFO("Wait for element '" + name + "' is performed");
            return element;
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    //</editor-fold>

    //<editor-fold desc="Wait Methods">

    public static WebElement waitForElementToBeClickable(By by) throws Exception {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Constant.WaitingSeconds);
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(by));
            logManger.INFO("Wait for element '" + by.toString() + "' is performed");
            return element;
        }
        catch(Exception ex){
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static void waitForAllElementsLocatedBy(By by) throws Exception {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Constant.WaitingSeconds);
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
            logManger.INFO("Wait for all elements located in " + by.toString() + " is performed");
        } catch (Exception e) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR("Class ActionHelper| Method waitForAllElementsLocatedBy | Exception occurred while waiting for presence of all elements located by " + by.toString() + ". "
                    + e.getMessage());
            throw e;
        }
    }

    public static void waitForInvisibilityOfElementLocated(By by) throws Exception {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Constant.WaitingSeconds);
            wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
        } catch (Exception e) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR("Class ActionHelper| Method waitForInvisibilityOfElementLocated | Exception occurred while waiting for invisibility of an element: "
                    + by.toString() + ". Error: " + e.getMessage());
            throw e;
        }
    }

    public static void waitForInvisibilityOfElementLocated(By by, int waitTimeSec) throws Exception {
        try {
            WebDriverWait wait = new WebDriverWait(driver, waitTimeSec);
            wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
        } catch (Exception e) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR("Class ActionHelper| Method waitForInvisibilityOfElementLocated | Exception occurred while waiting for invisibility of an element: "
                    + by.toString() + ". Error: " + e.getMessage());
            throw e;
        }
    }

    public static void waitForVisibilityOfElementLocated(By by) throws Exception {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Constant.WaitingSeconds);
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR("Class ActionHelper| Method waitForInvisibilityOfElementLocated | Exception occurred while waiting for invisibility of an element: "
                    + by.toString() + ". Error: " + e.getMessage());
            throw e;
        }
    }

    public static void waitUntilElementIsDisplayed(By elementlocator) throws Exception {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Constant.WaitingSeconds);
            wait.until(ExpectedConditions.visibilityOfElementLocated(elementlocator));
        } catch (Exception e) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR("Class ActionHelper| Method waitForInvisibilityOfElementLocated | Exception occurred while waiting for invisibility of an element: "
                    + elementlocator.toString() + ". Error: " + e.getMessage());
            throw e;
        }
    }

    public static void waitUntilElementIsDisplayed(By elementlocator, int waitTimeSec) throws Exception {
        try {
            WebDriverWait wait = new WebDriverWait(driver, waitTimeSec);
            wait.until(ExpectedConditions.visibilityOfElementLocated(elementlocator));
        } catch (Exception e) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR("Class ActionHelper| Method waitForInvisibilityOfElementLocated | Exception occurred while waiting for invisibility of an element: "
                    + elementlocator.toString() + ". Error: " + e.getMessage());
            throw e;
        }
    }

    public static void waitUntilElementIsEnabled(final By elementlocator) throws Exception {
        try {
            //@Override
            new WebDriverWait(driver, Constant.WaitingSeconds) {
            }.until((ExpectedCondition<Boolean>) driver -> (driver.findElement(elementlocator).isEnabled()));
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static void waitUntilInstancesEqual(final By elementLocator, final int instances) throws Exception {
        try {
            new WebDriverWait(driver, Constant.WaitingSeconds) {
            }.until(new ExpectedCondition<Boolean>() {
                //@Override
                public Boolean apply(WebDriver driver) {
                    return (driver.findElements(elementLocator).size() == instances);
                }
            });
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static void waitUntilInstancesEqual(final By elementLocator, final WebElement wParent, final int instances) throws Exception {
        try {
            new WebDriverWait(driver, Constant.WaitingSeconds) {
            }.until(new ExpectedCondition<Boolean>() {
                //@Override
                public Boolean apply(WebDriver driver) {
                    return (wParent.findElements(elementLocator).size() == instances);
                }
            });
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static void waitUntilInstancesAreMoreThan(final By elementLocator, final int instances) throws Exception {
        try {
            new WebDriverWait(driver, Constant.WaitingSeconds) {
            }.until(new ExpectedCondition<Boolean>() {
                //@Override
                public Boolean apply(WebDriver driver) {
                    return (driver.findElements(elementLocator).size() > instances);
                }
            });
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static void waitUntilInstancesAreMoreThan(final By elementLocator, final WebElement wParent, final int instances) throws Exception {
        try {
            new WebDriverWait(driver, Constant.WaitingSeconds) {
            }.until(new ExpectedCondition<Boolean>() {
                //@Override
                public Boolean apply(WebDriver driver) {
                    return (wParent.findElements(elementLocator).size() > instances);
                }
            });
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static void waitUntilTextPresentInElement(By by, String text) throws Exception {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Constant.WaitingSeconds);
            wait.until(ExpectedConditions.textToBePresentInElementLocated(by, text));
            logManger.INFO("Wait Until text Present in Element " + by.toString() + " is performed");
        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR("Class ActionHelper| Method waitUntilTextPresentInElement | Exception occurred while waiting for presence of all elements located by " + by.toString() + ". "
                    + ex.getMessage());
            throw ex;
        }
    }

    public static void waitUntilValuePresentInElement(By by, String text) throws Exception {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Constant.WaitingSeconds);
            wait.until(ExpectedConditions.textToBePresentInElementValue(by, text));
            logManger.INFO("Wait Until text Present in Element " + by.toString() + " is performed");
        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR("Class ActionHelper| Method waitUntilTextPresentInElement | Exception occurred while waiting for presence of all elements located by " + by.toString() + ". "
                    + ex.getMessage());
            throw ex;
        }
    }

    public static void waitUntilValuePresentInElement(WebElement wElement, String text) throws Exception {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Constant.WaitingSeconds);
            wait.until(ExpectedConditions.textToBePresentInElementValue(wElement, text));
            logManger.INFO("Wait Until text Present in Element " + wElement.toString() + " is performed");
        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR("Class ActionHelper| Method waitUntilTextPresentInElement | Exception occurred while waiting for presence of all elements located by " + wElement.toString() + ". "
                    + ex.getMessage());
            throw ex;
        }
    }

    public static void normalizeWaitingTimeForElement() throws Exception {
        try {
            driver.manage().timeouts().implicitlyWait(Constant.WaitingSeconds, TimeUnit.SECONDS);
            logManger.INFO("Set the implicit waiting time to " + Constant.WaitingSeconds
                    + " seconds");
        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR(ex.getMessage());
            throw ex;
        }
    }

    public static void waitForElementAfterClickMethod(By waitForLocator, By clickLocator, int ms, int retry) throws Exception {
        try {
            int count = 0;
            WebElement invoiceGrid = findElement(waitForLocator);
            while (invoiceGrid == null) {
                pauseTime(ms);
                invoiceGrid = findElement(waitForLocator);
                count++;
                if (count == retry) {
                    break;
                }
                clickElement(findElement(clickLocator));
            }
        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR(ex.getMessage());
            throw ex;
        }
    }

    public static void waitForElementAfterClickMethod(By waitForLocator, WebElement wElement, int ms, int retry) throws Exception {
        try {
            int count = 0;
            WebElement invoiceGrid = findElement(waitForLocator);
            while (invoiceGrid == null) {
                pauseTime(ms);
                invoiceGrid = findElement(waitForLocator);
                count++;
                if (count == retry) {
                    break;
                }
                clickElement(wElement);
            }
        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR(ex.getMessage());
            throw ex;
        }
    }

    public static void waitForElementAfterClickThroughJsMethod(By waitForLocator, WebElement wElement, int ms, int retry) throws Exception {
        try {
            int count = 0;
            WebElement invoiceGrid = findElement(waitForLocator);
            while (invoiceGrid == null) {
                pauseTime(ms);
                invoiceGrid = findElement(waitForLocator);
                count++;
                if (count == retry) {
                    break;
                }
                clickButtonThroughJS(wElement, "Click Element");
            }
        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR(ex.getMessage());
            throw ex;
        }
    }

    public static void waitForElementAfterClickThroughJsMethod(By waitForLocator, By clickElementLocator, int ms, int retry) throws Exception {
        try {
            int count = 0;
            WebElement invoiceGrid = findElement(waitForLocator);
            while (invoiceGrid == null) {
                pauseTime(ms);
                invoiceGrid = findElement(waitForLocator);
                count++;
                if (count == retry) {
                    break;
                }
                WebElement clickElement = findElement(clickElementLocator);
                if (clickElement != null) {
                    clickButtonThroughJS(clickElement, "Click Element");
                }
            }
        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR(ex.getMessage());
            throw ex;
        }
    }

    public static void implicitWait(int ms) throws Exception {
        try {
            driver.manage().timeouts().implicitlyWait(ms, TimeUnit.MILLISECONDS);
        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR(ex.getMessage());
            throw ex;
        }
    }

    //</editor-fold>

    //<editor-fold desc="Click Methods">

    public static void clickElement(WebElement element, String controlName) throws Exception {
        try {
            element.click();
            logManger.INFO("Click action is performed on '" + controlName + "' button");
        }
        catch (Exception ex){
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static void clickElement(WebElement element) throws Exception {
        try {
            element.click();
            logManger.INFO("Click action is performed...");
        }
        catch (Exception ex){
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static void clickElement(By by) throws Exception {
        try {
            driver.findElement(by).click();
            logManger.INFO("Click action is performed...");
        }
        catch (Exception ex){
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static void clickAction(WebElement element) throws Exception {
        try {
            Actions action = new Actions(driver);
            action.click(element).perform();
        }
        catch (Exception ex){
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static void doubleClick(WebElement element) throws Exception {
        try {
            Actions action = new Actions(driver);
            action.moveToElement(element).doubleClick().perform();
        }
        catch (Exception ex){
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }


    public static void clickElementActions(WebElement element) throws Exception {
        try {
            Actions action = new Actions(driver);
            action.click(element).perform();
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static void clickButtonThroughJS(WebElement element, String controlName) throws Exception {
        try {
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", element);
            logManger.INFO("Click action through javascript is performed on '" + controlName + "' button");
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static void doubleClickButtonThroughJS(WebElement element, String controlName) throws Exception {
        try {
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            String event = "var event=new MouseEvent(\"dblclick\",{view:window,bubbles:!0,cancelable:!0});document.querySelector(\"div[id='InProcessGrid']>div>table>tbody>tr.rowselected>td:nth-child(1)\").dispatchEvent(event);";
            //executor.executeScript("arguments[0].dispatchEvent(" + event + ");", element);
            executor.executeScript(event);
            logManger.INFO("Click action through javascript is performed on '" + controlName + "' button");
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }


    //</editor-fold>

    //<editor-fold desc="SendKeys Methods">

    public static void sendKeys(By locator, String text) throws Exception {
        try {
            logManger.INFO("Sendkeys " + text);
            WebElement wElement = findElement(locator);
            if (wElement != null)
                wElement.sendKeys(text);
            else
                throw new Exception("Can't find the Element, Locator: " + locator.toString());
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static void sendKeys(WebElement wElement, String text) throws Exception {
        try {
            wElement.sendKeys(text);
            logManger.INFO("Sendkeys " + text);
        }catch (Exception ex){
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static void sendKeysWithClear(WebElement wElement, String text) throws Exception {
        try {
            wElement.clear();
            wElement.sendKeys(text);
            logManger.INFO("Sendkeys with Clear");
        }catch (Exception ex){
            getScreenshot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR(ex.getStackTrace().toString());
            throw ex;
        }
    }

    public static void sendKeys(WebElement wElement, Keys key) throws Exception {
        try {
            wElement.sendKeys(key);
            logManger.INFO("Sendkeys " + key);
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static void sendKeys(By locator, Keys key) throws Exception {
        try {
            driver.findElement(locator).sendKeys(key);
            logManger.INFO("Sendkeys " + key);
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

//    public static void sendKeys(int rowNumber, WebElement element, String columnName) throws Exception {
//        try {
//            sendKeys(rowNumber, element, columnName, columnName);
//        }catch (Exception ex){
//            logManger.ERROR(ex.getMessage());
//            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
//            throw ex;
//        }
//    }

//    public static void sendKeys(int rowNumber, WebElement element, String excelColumnName, String elementName) throws Exception {
//        try {
//            String dataValue = ExcelUtils.getCellData(rowNumber, excelColumnName);
//            if (!dataValue.isEmpty())
//                sendKeys(element, elementName, dataValue);
//            logManger.INFO("Sendkeys " + dataValue);
//        }catch (Exception ex){
//            logManger.ERROR(ex.getMessage());
//            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
//            throw ex;
//        }
//    }

    public static void sendKeys(WebElement element, String elementName, String dataValue) throws Exception {
        try {
            element.clear();
            element.sendKeys(dataValue);
            logManger.INFO("'" + dataValue + "' is entered in '" + elementName + "' input");
        }catch (Exception ex){
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

//    public static void sendKeysThroughJS(int rowNumber, WebElement element, String columnName) throws Exception {
//        try {
//            sendKeysThroughJS(rowNumber, element, columnName, columnName);
//        }catch (Exception ex){
//            logManger.ERROR(ex.getMessage());
//            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
//            throw ex;
//        }
//    }

//    public static void sendKeysThroughJS(int rowNumber, WebElement element, String columnName, String fieldName) throws Exception {
//        try {
//            String dataValue = ExcelUtils.getCellData(rowNumber, columnName);
//            ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1]", element, dataValue);
//            logManger.INFO("'" + dataValue + "' is entered in '" + fieldName + "' input");
//        }catch (Exception ex){
//            logManger.ERROR(ex.getMessage());
//            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
//            throw ex;
//        }
//    }

    public static void sendKeysThroughJS(String dataValue, WebElement element, String fieldName) throws Exception {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1]", element, dataValue);
            logManger.INFO("'" + dataValue + "' is entered in '" + fieldName + "' input");
        }catch (Exception ex){
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static void editHTMLText(String dataValue, WebElement element) throws Exception {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].innerHTML = arguments[1]", element, dataValue);
        }catch (Exception ex){
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static void editValueThroughJS(String dataValue, WebElement element) throws Exception {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1]", element, dataValue);
        }catch (Exception ex){
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    //</editor-fold>

    //<editor-fold desc="Check/Uncheck Methods">

    public static void checkElement(By locator) throws Exception {
        try {
            WebElement checkBox = driver.findElement(locator);
            if (!checkBox.getAttribute("type").toLowerCase().equals("checkbox")) {
                throw new InvalidElementTypeException("This elementLocator is not a checkbox!");
            }
            if (!checkBox.isSelected()) {
                checkBox.click();
            }
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static void checkElement(WebElement checkBox) throws Exception {
        try {
            if (!checkBox.getAttribute("type").toLowerCase().equals("checkbox")) {
                throw new InvalidElementTypeException("This elementLocator is not a checkbox!");
            }
            if (!checkBox.isSelected()) {
                checkBox.click();
            }
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static void unCheckElement(By locator) throws Exception {
        try {
            WebElement checkBox = driver.findElement(locator);
            if (!checkBox.getAttribute("type").toLowerCase().equals("checkbox")) {
                throw new InvalidElementTypeException("This elementLocator is not a checkbox!");
            }
            if (checkBox.isSelected()) {
                checkBox.click();
            }
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static void unCheckElement(WebElement checkBox) throws Exception {
        try {
            if (!checkBox.getAttribute("type").toLowerCase().equals("checkbox")) {
                throw new InvalidElementTypeException("This elementLocator is not a checkbox!");
            }
            if (checkBox.isSelected()) {
                checkBox.click();
            }
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static boolean isChecked(By locator) throws Exception {
        try {
            WebElement checkBox = driver.findElement(locator);
            if (!checkBox.getAttribute("type").toLowerCase().equals("checkbox")) {
                throw new InvalidElementTypeException("This elementLocator is not a checkbox!");
            }
            return checkBox.getAttribute("checked").equals("checked");
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            return false;
        }
    }

//    public static void Check(int rowNumber, WebElement element, String columnName) throws Exception {
//        try {
//            Check(rowNumber, element, columnName, columnName);
//        } catch (Exception ex) {
//            logManger.ERROR(ex.getMessage());
//            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
//            throw ex;
//        }
//    }

//    public static void Check(int rowNumber, WebElement element, String columnName, String fieldName) throws Exception {
//        String dataValue = ExcelUtils.getCellData(rowNumber, columnName);
//        if ("Yes".equalsIgnoreCase(dataValue)) {
//            if (!element.isSelected())
//                element.click();
//            logManger.INFO("It is checked '" + fieldName + "' checkElement box");
//        } else {
//            if (element.isSelected())
//                element.click();
//            logManger.INFO("It is not checked '" + fieldName + "' checkElement box");
//        }
//    }

    //</editor-fold>

    //<editor-fold desc="SelectOption Methods">

//    public static void selectOption(int rowNumber, WebElement element, String columnName) throws Exception {
//        try {
//            selectOption(rowNumber, element, columnName, columnName);
//        } catch (Exception ex) {
//            logManger.ERROR(ex.getMessage());
//            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
//            throw ex;
//        }
//    }

//    public static void selectOption(int rowNumber, WebElement element, String columnName, String fieldName) throws Exception {
//        Boolean founded = false;
//        String fullText = "";
//        String dataValue = ExcelUtils.getCellData(rowNumber, columnName);
//        Select select = new Select(element);
//        List<WebElement> list = select.getOptions();
//        for (WebElement option : list) {
//            fullText = option.getText();
//            if (fullText.contains(dataValue)) {
//                select.selectByVisibleText(fullText);
//                founded = true;
//                break;
//            }
//        }

//        if (founded)
//            logManger.INFO(String.format("'" + fullText + "' is selected in '" + fieldName + "' drop down list", dataValue));
//        else {
//            logManger.ERROR("Option '" + fullText + "' is not found in '" + fieldName + "' drop down list");
//            // I put this line intentionally to generate an exception.
//            (new Select(element)).selectByVisibleText(dataValue);
//        }
//    }

    public static void selectByValue(By locator, String value) throws Exception {
        try {
            Select select = new Select(driver.findElement(locator));
            select.selectByValue(value);
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static void selectByValue(WebElement welement, String value) throws Exception {
        try {
            Select select = new Select(welement);
            select.selectByValue(value);
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static void selectByIndex(By locator, int index) throws Exception {
        try {
            Select select = new Select(driver.findElement(locator));
            select.selectByIndex(index);
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static void selectByIndex(WebElement webElement, int index) throws Exception {
        try {
            Select select = new Select(webElement);
            select.selectByIndex(index);
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static void selectByVisibleText(By locator, String text) throws Exception {
        try {
            Select select = new Select(driver.findElement(locator));
            select.selectByVisibleText(text);
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static void selectByVisibleText(WebElement welement, String text) throws Exception {
        try {
            Select select = new Select(welement);
            select.selectByVisibleText(text);
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static boolean doesElementExist(By locator) throws Exception {
        try {
            return driver.findElements(locator).size() > 0;
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            return false;
        }

    }

    //</editor-fold>

    //<editor-fold desc="Element Methods">

    public static WebElement findElement(By locator) throws Exception{
        try {
            if (driver.findElements(locator).size() > 0) {
                return driver.findElement(locator);
            } else {
                return null;
            }
        }catch(Exception ex){
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            return null;
        }
    }

    public static List<WebElement> findElements(By locator) throws Exception{
        try{
            if (driver.findElements(locator).size() > 0) {
                return driver.findElements(locator);
            } else {
                return null;
            }
        }catch (Exception ex)
        {
            logManger.ERROR("Class ActionHelper| Method pauseTime | Exception occurred: Exception: " + ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static boolean isElementDisplayed(By locator) throws Exception {
        try {
            if (doesElementExist(locator)) {
                return driver.findElement(locator).isDisplayed();
            } else {
                return false;
            }
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static int getElementCount(By locator) throws Exception {
        try {
            java.util.List elementsFound = driver.findElements(locator);
            return elementsFound.size();
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static Boolean existsElement(By by, Boolean extraTime) throws Exception {
        Boolean found = false;
        try {
            WebElement element = findElement(by);
            if (element != null)
                found = true;

            if (extraTime)
                normalizeWaitingTimeForElement();

        } catch (Exception e) {
            logManger.ERROR(e.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            if (extraTime)
                normalizeWaitingTimeForElement();
        }
        return found;
    }

    public static void clickAndHold(By locator) throws Exception {
        try {
            WebElement webElement = driver.findElement(locator);

            if (webElement != null) {
                new Actions(driver).clickAndHold(webElement).perform();
            }
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static void clickAndHold(WebElement webElement) throws Exception {
        try {
            if (webElement != null) {
                new Actions(driver).clickAndHold(webElement).perform();
            }
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static void releaseElement(By locator) throws Exception {
        try {
            WebElement webElement = driver.findElement(locator);

            if (webElement != null) {
                new Actions(driver).release(webElement).perform();
            }
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static void releaseElement(WebElement webElement) throws Exception {
        try {
            if (webElement != null) {
                new Actions(driver).release(webElement).perform();
            }
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static void clickAt(By locator, int x, int y) throws Exception {
        try {
            WebElement webElement = driver.findElement(locator);

            if (webElement != null) {
                Actions builder = new Actions(driver);
                builder.moveToElement(webElement).moveByOffset(x, y).click().perform();
            }
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static void clickAt(WebElement webElement, int x, int y) throws Exception {
        try {
            if (webElement != null) {
                Actions builder = new Actions(driver);
                builder.moveToElement(webElement).moveByOffset(x, y).click().perform();
            }
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static void submitElement(By locator) throws Exception {
        try {
            WebElement webElement = driver.findElement(locator);

            if (webElement != null) {
                webElement.submit();
            }
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static void submitElement(WebElement webElement) throws Exception {
        try {
            if (webElement != null) {
                webElement.submit();
            }
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
    }

    public static WebElement getElement(By by, String name, String type, String place) throws Exception {
        return getElement(by, name, type, place, null);
    }

    public static WebElement getElement(By by, String name, String type, String place, WebElement father) throws Exception {
        WebElement element;
        String messageSuccess = "Found - Element: '" + name + "', Type: '" + type + "', In: '" + place + "'";
        String messageError = "Not Found - Element: '" + name + "', Type: '" + type + "', In: '" + place + "'";

        try {
            if (father != null)
                element = father.findElement(by);
            else
                element = driver.findElement(by);
            logManger.INFO(messageSuccess);
        } catch (Exception ex) {
            logManger.ERROR(messageError);
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
        return element;
    }

    public static String getCssValue(WebElement webElement, String value) throws Exception {
        try {
            if (webElement != null) {
                return webElement.getCssValue(value);
            }
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            throw ex;
        }
        return null;
    }

    public static void goToURL(String url) throws Exception {
        try {
            logManger.INFO("Executing Goto method...");
            //String fullUrl = Constant.URL + url;
            String fullUrl = "";//Environments.get() + url;
            logManger.INFO("Full URL: " + fullUrl);
            if (driver == null) {
                logManger.ERROR("Driver is null");
                throw new Exception("Driver is null");
            }
            System.out.println(fullUrl);
            driver.get(fullUrl);
            logManger.INFO("Going to " + fullUrl);
        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR("Class ActionHelper| Method goToURL | Exception occurred while going to a page : "
                    + ex.getMessage());
            throw ex;
        }
    }

    //</editor-fold>

    //<editor-fold desc="Windows Methods">

    public static void displayWindow(WebElement button, String buttonName, By window) throws Exception {
        try {
            pauseTime(3000);
            clickElement(button, buttonName);
            // Check if the Dialog is displayed
            if (!existsElement(window, false)) {
                Thread.sleep(3000);
                clickElement(button); // If the Add dialog does not appear, click add button again
                logManger.INFO("Click action is performed on '" + buttonName + "' button by second time");
                waitForAllElementsLocatedBy(window);
            }
        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR("Class ActionHelper| Method displayWindow | Exception: " + ex.getMessage());
            throw ex;
        }
    }

    public static void scrollToElement(By locator) throws Exception {
        try {
            Locatable scrollToItem = (Locatable) driver.findElement(locator);
            int y = scrollToItem.getCoordinates().inViewPort().getY();
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0," + y + ");");
        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR(ex.getMessage());
            throw ex;
        }
    }

    public static void scrollToElement(WebElement wElement) throws Exception {
        try {
            Locatable scrollToItem = (Locatable) wElement;
            int y = scrollToItem.getCoordinates().inViewPort().getY();
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0," + y + ");");
        } catch (Exception ex) {
            logManger.ERROR(ex.getMessage());
            throw ex;
        }
    }

    public static void moveToElementByXpath(String xpath) throws Exception {
        try {
            WebElement mapObject = driver.findElement(By.xpath(xpath));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", mapObject);
        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR(ex.getMessage());
            throw ex;
        }
    }

    public static void moveToElementByCssSelector(String selector) throws Exception {
        try {
            WebElement mapObject = driver.findElement(By.cssSelector(selector));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", mapObject);
        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR(ex.getMessage());
            throw ex;
        }
    }

    public static void moveToElement(WebElement wElement) throws Exception {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", wElement);
        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR(ex.getMessage());
            throw ex;
        }
    }

    public static void moveToElementByActions(By locator) throws Exception {
        try {
            WebElement element = driver.findElement(locator);
            Actions actions = new Actions(driver);
            actions.moveToElement(element);
            actions.perform();
        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR(ex.getMessage());
            throw ex;
        }
    }

    public static void moveToElementByActions(WebElement wElement) throws Exception {
        try {
            Actions actions = new Actions(driver);
            actions.moveToElement(wElement);
            actions.perform();
        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR(ex.getMessage());
            throw ex;
        }
    }

    public static Boolean switchBetweenWindows() throws Exception {
        try {
            Set listOfWindows = driver.getWindowHandles();
            if (listOfWindows.size() != 2) {
                if (listOfWindows.size() > 2) {
                    throw new TooManyWindowsException();
                } else {
                    throw new TooFewWindowsException();
                }
            }
            String currentWindow = driver.getWindowHandle();
            for (Object listOfWindow : listOfWindows) {
                String selectedWindowHandle = listOfWindow.toString();
                if (!selectedWindowHandle.equals(currentWindow)) {
                    driver.switchTo().window(selectedWindowHandle);
                    return true;
                }
            }
            // Just in case something goes wrong
            throw new UnableToFindWindowException("Unable to switch windows!");

        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR(ex.getMessage());
            return null;
        }
    }

    public static Boolean switchToWindowTitled(String windowTitle) throws Exception {
        try {
            driver.switchTo().window(windowTitle);
            return true;
        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR(ex.getMessage());
            return null;
        }
    }

    public static void maximizeWindow() throws Exception {
        try {
            driver.manage().window().maximize();
        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR(ex.getMessage());
            throw ex;
        }
    }

    public static boolean isAlertPresent() throws Exception {
        boolean alertPresent = false;
        try {
            WebDriverWait wait = new WebDriverWait(driver, Constant.WaitingSeconds);
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert();
            alertPresent = true;
        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR(ex.getMessage());
            throw ex;
        }
        return alertPresent;
    }

    public static void dismissAlert() throws Exception {
        try {
            Alert alert = driver.switchTo().alert();
            alert.dismiss();
        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR(ex.getMessage());
            throw ex;
        }
    }

    public static String getPageTitle() throws Exception {
        String title = "";
        try {
            title = driver.getTitle();
        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR(ex.getMessage());
            return null;
        }
        return title;
    }

    public static String getWindowURL() throws Exception {
        String url;
        try {
            url = driver.getCurrentUrl();
            return url;
        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR(ex.getMessage());
            return null;
        }
    }

    public static void closeBrowser() throws Exception {
        try {
            driver.close();
        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR(ex.getMessage());
            throw ex;
        }
    }

    public static void getBackInBrowser() throws Exception {
        try {
            driver.navigate().back();
        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR(ex.getMessage());
            throw ex;
        }

    }

    public static void getForwardInBrowser() throws Exception {
        try {
            driver.navigate().forward();
        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR(ex.getMessage());
            throw ex;
        }
    }

    public static void deleteAllCookies() throws Exception {
        try {
            driver.manage().deleteAllCookies();
        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR(ex.getMessage());
            throw ex;
        }
    }

    public static void refreshPage() throws Exception {
        try {
            driver.navigate().refresh();
        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR(ex.getMessage());
            throw ex;
        }
    }

    public static void executeJS(String jsCode) throws Exception {
        try {
            ((JavascriptExecutor) driver).executeScript(jsCode);
        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR("Class ActionHelper| Method executeJS | Exception occurred while executing javascript. Code: "
                    + jsCode + "   Exception: " + ex.getMessage());
            throw ex;
        }
    }

    //</editor-fold>

    //<editor-fold desc="Java Script Methods">

    public static String generateTimestamp() throws Exception {
        try {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmssSS");
            String value = simpleDateFormat.format(new Date());
            if (value.length() < 17) {
                value = simpleDateFormat.format(new Date());
            }
            return value;
        } catch (Exception ex) {
            logManger.ERROR("Class ActionHelper| Method generateTimestamp | Exception occurred: Exception: " + ex.getMessage());
            throw ex;
        }
    }

    //</editor-fold>

    //<editor-fold desc="WebdriverUtils Methods">

    public static void pauseTime(int timeToSleep) throws Exception {
        try{
            Thread.sleep(timeToSleep);
        }catch (Exception ex)
        {
            logManger.ERROR("Class ActionHelper| Method pauseTime | Exception occurred: Exception: " + ex.getMessage());
            throw ex;
        }
    }

    public static String getTestCaseName(String sTestCase) throws Exception {
        String value = sTestCase;
        try {
            int posi = value.indexOf("@");
            value = value.substring(0, posi);
            posi = value.lastIndexOf(".");
            value = value.substring(posi + 1);
            return value;
        } catch (Exception ex) {
            logManger.ERROR("Class ActionHelper| Method getTestCaseName | Exception desc : "
                    + ex.getMessage());
            throw ex;
        }
    }

//    public static void //captureScreenShot(WebDriver driver, String methodName)throws Exception{
//        try {
//            //logger.info("Take Screenshot");
//            DateFormat dfn = new SimpleDateFormat("yyyy/MM/dd");
//            DateFormat df = new SimpleDateFormat("MMM . dd . yyyy _HH:mm:ss");
//
//            String formattedDate = df.format(new Date());
//            String folderName = dfn.format(new Date());
//
//            //ScreenShots will be saved in different levels in folders, i.e, /#YEAR/#MONTH/#DAY.jpg
//            formattedDate = formattedDate.replace(" ", "");
//            formattedDate = formattedDate.replace(":", "");
//            String filePath = "src/test/java/screenShots/" + folderName.replace("/", "") + "/" + getTestName() + "/" + methodName + "_" + formattedDate + ".jpg";
//
//            java.io.File screenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//            FileUtils.copyFile(screenShot, new File(filePath));
//        } catch (Exception ex) {
//            logManger.ERROR("Class ActionHelper| Method Capture Screenshot | Exception desc : "
//                    + ex.getMessage());
//            throw ex;
//        }
//    }

    public static String getDate(int days) {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, days);
        Date tmpDate = calendar.getTime();
        return dateFormat.format(tmpDate);
    }

    //</editor-fold>

    //<editor-fold desc="Grid Methods">

    public static int getColumnIndex(String gridName, String columnHeader) throws Exception {
        String jsCode = "";
        try {
            jsCode = " var columnHeader = '" + columnHeader + "'; "
                    + " var mygrid = $('" + gridName + "').grid; "
                    + " for (var i = 0; i < mygrid.getColumnsNum(); i++) {  "
                    + " var colLabel = mygrid.getColLabel(i).trim();  "
                    + " if( colLabel.toUpperCase() == columnHeader.toUpperCase() ) { "
                    + " return i;  "
                    + " }  "
                    + " }  "
                    + " return -1;";
            int columnIndex = ((Long) ((JavascriptExecutor) driver).executeScript(jsCode)).intValue();
            if (columnIndex < 0) {
                throw new Exception("Column " + columnHeader + " not found");
            }

            return columnIndex;
        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR("Class ActionHelper| Method getColumnIndex | Exception occurred while executing javascript. Code: "
                    + jsCode + "   Exception: " + ex.getMessage());
            throw ex;
        }
    }

    public static WebElement getTxtFilter(String gridName, String columnHeader) throws Exception {
        String jsCode = "";
        int index = getColumnIndex(gridName, columnHeader);

        try {
            jsCode = "return $('" + gridName + "').grid.getFilterElement(" + index + ");";
            WebElement filter = (WebElement) ((JavascriptExecutor) driver).executeScript(jsCode);
            if (filter == null) {
                throw new Exception("Filter textbox is not found. Grid: " + gridName + ", ColumnHeader: " + columnHeader);
            }
            return filter;
        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR("Class ActionHelper| Method getTxtFilter | Exception occurred while executing javascript. Code: "
                    + jsCode + "   Exception: " + ex.getMessage());
            throw ex;
        }
    }

    public static String getGridCellValue(String gridName, String columnHeader, int moveFromIndex) throws Exception {
        String jsCode = "";
        try {

            int columnIndex = getColumnIndex(gridName, columnHeader) + moveFromIndex;

            jsCode = "return $('" + gridName + "').grid.cells($('" + gridName + "').grid.getSelectedRowId()," + columnIndex + ").getValue(); ";
            return (String) ((JavascriptExecutor) driver).executeScript(jsCode);

        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR("Class ActionHelper| Method getGridCellValue | Exception occurred while executing javascript. Code: "
                    + jsCode + "   Exception: " + ex.getMessage());
            throw ex;
        }
    }

    public static int getGridRowNumber(String gridName) throws Exception {
        int count = -1;
        String jsCode = "return $('" + gridName + "').grid.getRowsNum();";
        try {
            count = ((Long) ((JavascriptExecutor) driver).executeScript(jsCode)).intValue();
        } catch (Exception ex) {
            //captureScreenShot(driver, new Exception().getStackTrace()[0].getMethodName());
            logManger.ERROR("Class ActionHelper| Method getGridRowNumber | Exception occurred while executing javascript. Code: "
                    + jsCode + "   Exception: " + ex.getMessage());
            throw ex;
        }
        return count;
    }

    public enum BrowserEnum {
        FIREFOX,
        CHROME,
        IE11,
        IE10,
        IE9,
        SAFARI,
        SAUCELABS
    }

    //driver and screenshotName
    public static String getScreenshot(WebDriver driver, String screenshotName) throws Exception {
        //below line is just to append the date format with the screenshot name to avoid duplicate names
        String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        //after execution, you could see a folder "FailedTestsScreenshots" under src folder
        String destination =System.getProperty("user.dir") + "/src/main/resources/Reports/"+screenshotName+dateName+".png";
        logManger.Capture(destination);
        File finalDestination = new File(destination);
        FileUtils.copyFile(source, finalDestination);
        //Returns the captured file path
        return destination;
    }}



