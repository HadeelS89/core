package com.qpros.helpers;


import com.qpros.common.Base;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


//import static com.qpros.common.Base.driver;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

public class FileHelper extends Base {

    /**
     * general method to upload file
     * @param inputXpath the file selector path
     * @param fileDirectory  local file directory
     * @param uploadButtonXpath selector for upload button
     * @param conditionXpath if upload success locator should be viewed
     * @throws InterruptedException
     */
    public void uploadFile(String inputXpath, String fileDirectory, String uploadButtonXpath, String conditionXpath) throws InterruptedException {
        driver.findElement(By.xpath(inputXpath)).click();
        WebElement addFile;


        driver.findElement(By.linkText("Set new profile picture")).click();
        driver.wait(2000); // Image name can be of your choice

        if (driver.findElement(By.xpath(conditionXpath)).isDisplayed()) {
            assertTrue(true, "File is Uploaded");
        } else {
            fail("File not Uploaded");
        }
    }

}

