package com.qpros.helpers;
import java.io.File;

import org.apache.commons.io.FileUtils;

import org.openqa.selenium.OutputType;

import org.openqa.selenium.TakesScreenshot;

import org.openqa.selenium.WebDriver;

public class ScreenshotHelper {

    public static void takeSnapShot(WebDriver webdriver,String fileWithPath){
        //Example usage:         ScreenshotHelper.takeSnapShot(driver, "C:\\Users\\HamzahAlrawi\\Desktop\\MyImage.png");
        TakesScreenshot scrShot =((TakesScreenshot)webdriver);
        File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
        File DestFile=new File(fileWithPath);
        try {
            FileUtils.copyFile(SrcFile, DestFile);
        }catch (Exception e){

        }
    }

}
