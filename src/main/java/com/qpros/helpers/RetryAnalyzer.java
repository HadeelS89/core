package com.qpros.helpers;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    int counter = 0;
    String max_retry_count = ReadWriteHelper.ReadData("MaxRetryCount");
    final int maxRetryCount = Integer.parseInt( max_retry_count );

    @Override
    public boolean retry(ITestResult result) {

        if((counter < maxRetryCount))
        {
            counter++;
            return true;
        }
        return false;
    }
}
