package com.qpros.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.qpros.helpers.EmailHelper;
import com.qpros.helpers.ReadWriteHelper;
import lombok.SneakyThrows;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class Listeners extends TestListenerAdapter{

    private static ExtentReports extent = ExtentManager.createInstance();
    public static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    ExceptionListner exceptionListener = new ExceptionListner();
    public static boolean CONSOLE;


    @Override
    public synchronized void onStart(ITestContext context) {
        if (CONSOLE) {

            System.out.println("Test Suite started!");
        }

    }

    @SneakyThrows
    @Override
    public synchronized void onFinish(ITestContext context) {

        if (CONSOLE) {
            System.out.println(("Test Suite is ending!"));

        }
        extent.flush();

        EmailHelper sender = new EmailHelper();

        String emails = ReadWriteHelper.ReadData( "EmailsTo" );
        String toSendEmails = ReadWriteHelper.ReadData( "SendMail" );
        System.out.println(emails);
        if (!emails.equals("") && toSendEmails.equals("true")) {
            //String test = "Hello,This,Is,A,Test"
            //String[] desired = test.split(",");
            //system.out.prinln(desired[1]);
            String[] emailArray = emails.split(",");
            for(String email: emailArray) {
                sender.sendMail(email , ExtentManager.path + ExtentManager.reportFileName);
            }

        }
    }

    @Override
    public synchronized void onTestStart(ITestResult result) {
        result.getTestClass().getXmlClass().getAllParameters();
        //result.getTestClass().getXmlClass().m_xmlTest.m_suite.m_fileNam;
        if (CONSOLE) {
            System.out.println((result.getMethod().getMethodName() + " started!"));

        }
        ExtentTest extentTest = extent.createTest(result.getMethod().getDescription(),
                result.getMethod().getMethodName());
        test.set(extentTest);
    }

    @Override
    public synchronized void onTestSuccess(ITestResult result) {
        if (CONSOLE) {
            System.out.println((result.getMethod().getMethodName() + " passed!"));
        }

        test.get().pass("Test passed");
    }

    @Override
    public synchronized void onTestFailure(ITestResult result) {
        if (CONSOLE) {
            System.out.println((result.getMethod().getMethodName() + " failed!"));
        }

        test.get().fail(exceptionListener.checkException(result.getThrowable().toString()));

    }

    @Override
    public synchronized void onTestSkipped(ITestResult result) {
        if (CONSOLE) {
            System.out.println((result.getMethod().getMethodName() + " skipped!"));
        }
        test.get().skip(exceptionListener.checkException(result.getThrowable().toString()));
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        if (CONSOLE) {
            System.out.println(("onTestFailedButWithinSuccessPercentage for " + result.getMethod().getMethodName()));

        }

    }
}
