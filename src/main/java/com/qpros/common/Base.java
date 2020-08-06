package com.qpros.common;


import com.qpros.helpers.ReadWriteHelper;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.HashMap;
import java.util.Map;

public class Base {

    protected static WebDriver driver;
    public boolean isHeadless= ReadWriteHelper.ReadData("headless").equalsIgnoreCase("true");

    /**
     * Set UP Browser it initiate the driver based on client OS and Browser Type
     * @param browserType String Value of Browser name from thr config File
     */
    @Parameters({"browserType"})
    @BeforeMethod(enabled = true)
    public void setUpBrowser(@Optional("optional") String browserType) {
        //DriverType browser = getBrowser();
        if (!browserType.equals( "optional" )){
            initiateDriver(OsValidator.getDeviceOs(), browserType);
        }else {
            initiateDriver(OsValidator.getDeviceOs(), ReadWriteHelper.ReadData( "browser" ) );
        }
    }
    /**
     * sub Method from the Setup Browser Method
     * @param deviceOsType String Device OS
     * @param driverType String Driver type Name
     * @return instance of WebDriver
     */
    private WebDriver initiateDriver(String deviceOsType, String driverType) {
        String browser = driverType.toLowerCase();
        switch (browser) {
            case "firefox":
                try {
                    setFireFoxBrowser(deviceOsType);
                    FirefoxOptions options = new FirefoxOptions(  );
                    options.setAcceptInsecureCerts( true );
                    if (ReadWriteHelper.ReadData( "headless" ).equalsIgnoreCase( "true" )){
                        options.addArguments("--headless");
                    }
                    driver = new FirefoxDriver(options);
                } catch (Throwable e) {
                    e.printStackTrace(System.out);
                    Assert.fail("Please check Browser is exist Browser Unable to start");
                }
                break;
            case "chrome":
                try {
                    DesiredCapabilities handlSSLErr = DesiredCapabilities.chrome ();
                    handlSSLErr.setCapability ( CapabilityType.ACCEPT_SSL_CERTS, true);
                    setChromeBrowser(deviceOsType);
                    Map<String, Object> prefs = new HashMap<>();
                    //Put this into prefs map to switch off browser notification
                    prefs.put("profile.default_content_setting_values.notifications", 2);
                    //Create chrome options to set this prefs
                    ChromeOptions options = new ChromeOptions();
                    options.setExperimentalOption("prefs", prefs);
                    options.addArguments("--disable-web-security");
                    options.addArguments("--allow-running-insecure-content");
                    options.setAcceptInsecureCerts( true );
                    if (ReadWriteHelper.ReadData( "headless" ).equalsIgnoreCase( "true" )){
                        options.addArguments("--headless");
                        options.addArguments("--proxy-server='direct://'");
                        options.addArguments("--proxy-bypass-list=*");
                        options.addArguments("--no-proxy-server");
                        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                    }
                    driver = new ChromeDriver(options);
                    //Dimension targetSize = new Dimension(1920, 1080); //your screen resolution here
                    //driver.manage().window().setSize(targetSize);

                } catch (Throwable e) {
                    e.printStackTrace(System.out);
                    Assert.fail("Please check Browser is exist Browser Unable to start");
                }
                break;
            case "ie":

            {
                try {
                    System.setProperty(ReadWriteHelper.ReadData("IEDriverPath"),
                            ReadWriteHelper.ReadData("IEBrowserPathWindows"));
                    driver = new InternetExplorerDriver();
                } catch (Throwable e) {
                    e.printStackTrace(System.out);
                    Assert.fail("Please check Browser is exist Browser Unable to start");
                }
                break;
            }
            case "safari": {
                try {
                    System.setProperty(ReadWriteHelper.ReadData("SafariDriverPath"),
                            ReadWriteHelper.ReadData("SafariBrowserPath"));

                    driver = new SafariDriver();
                } catch (Throwable e) {
                    e.printStackTrace(System.out);
                    Assert.fail("Please check Browser is exist Browser Unable to start");
                }
            }
        }

        driver.manage().window().maximize();
        return driver;
    }

    public DriverType getBrowser() {
        String browserName = ReadWriteHelper.ReadData("browser");

        if (browserName == null || browserName.equalsIgnoreCase("chrome"))
            return DriverType.CHROME;
        else if (browserName.equalsIgnoreCase("firefox"))
            return DriverType.FIREFOX;
        else if (browserName.equals("iexplorer") || browserName.equalsIgnoreCase("internetExplorer") ||
                browserName.equalsIgnoreCase("ie"))
            return DriverType.INTERNETEXPLORER;
        else if (browserName.equalsIgnoreCase("safari")) return DriverType.Safari;
        else
            throw new RuntimeException("Browser Name Key value in Configuration.properties is not matched : " +
                    browserName);
    }


    private void setChromeBrowser(String deviceOsType) {
        if (deviceOsType.equalsIgnoreCase("mac")) {
            System.setProperty(ReadWriteHelper.ReadData("ChromeDriverPath"),
                    ReadWriteHelper.ReadData("ChromeDriverLinkMac"));
        } else if (deviceOsType.equalsIgnoreCase("windows")) {
            System.setProperty(ReadWriteHelper.ReadData("ChromeDriverPath"),
                    ReadWriteHelper.ReadData("chromeDriverLinkWindows"));
        }
        else if(deviceOsType.equalsIgnoreCase("Unix"))
        {
            System.setProperty(ReadWriteHelper.ReadData("ChromeDriverPath"),
                    ReadWriteHelper.ReadData("chromeDriverLinkLinux"));
        }
    }

    private void setFireFoxBrowser(String deviceOsType) {
        if (deviceOsType.equalsIgnoreCase("mac")) {
            System.setProperty(ReadWriteHelper.ReadData("FireFoxDriverPath"),
                    ReadWriteHelper.ReadData("FireFoxBrowserPathMac"));
        } else if (deviceOsType.equalsIgnoreCase("windows")) {
            System.setProperty(ReadWriteHelper.ReadData("FireFoxDriverPath"),
                    ReadWriteHelper.ReadData("FireFoxBrowserPathWindows"));
        }
    }

    //@AfterMethod(enabled = true)
    public void stopDriver() {
        try {
            driver.quit();
        } catch (Throwable e) {
            e.getStackTrace();
        }
    }
}
