package com.qpros.common;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.qpros.reporting.ExceptionListner;
import com.qpros.reporting.ExtentManager;
import com.qpros.reporting.ExtentTestManager;
import com.qpros.utils.ConsoleColors;
import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class LogManger implements ITestListener {

    static Logger logger = Logger.getLogger(LogManger.class.getSimpleName());
    private static final ExtentReports extent = ExtentManager.createInstance();
    public static ThreadLocal<ExtentTest> test = new ThreadLocal<>();


    public LogManger() {
    }

    public LogManger(String classObjName) {
        logger = Logger.getLogger(classObjName);
    }

    public void INFO(String message) {
        logger.info(ConsoleColors.BLUE + message + ConsoleColors.RESET);
        ExtentTestManager.getTest().log(Status.INFO, message);
    }

    public void Capture(String Path) {
        ExtentTestManager.getTest().addScreenCaptureFromPath(Path);
    }

    public void INFO(Integer message) {
        logger.info(ConsoleColors.BLUE + message + ConsoleColors.RESET);
        ExtentTestManager.getTest().log(Status.INFO, String.valueOf(message));

    }

    public void DEBUG(String message) {
        logger.debug(ConsoleColors.GREEN + message + ConsoleColors.RESET);
        ExtentTestManager.getTest().log(Status.INFO, message);

    }

    public void WARN(String message) {
        logger.warn(ConsoleColors.YELLOW + message + ConsoleColors.RESET);
        ExtentTestManager.getTest().log(Status.WARNING, message);

    }

    public void ERROR(String message) {
        logger.info(ConsoleColors.RED + message + ConsoleColors.RESET);
        ExtentTestManager.getTest().log(Status.WARNING, message);

    }

    public void FATAL(String message) {
        logger.info(ConsoleColors.RED_BACKGROUND + message + ConsoleColors.RESET);
        ExtentTestManager.getTest().log(Status.FAIL, message);
    }

    public void TRACE(String message) {
        logger.trace(ConsoleColors.PURPLE + message + ConsoleColors.RESET);
        ExtentTestManager.getTest().log(Status.WARNING, message);

    }

    public void INFO(String message, Exception exception) {
        logger.info(ConsoleColors.BLUE + message + ConsoleColors.RESET, exception);
        ExtentTestManager.getTest().log(Status.INFO, message);

    }

    public void DEBUG(String message, Exception exception) {
        logger.debug(ConsoleColors.GREEN + message + ConsoleColors.RESET, exception);
        ExtentTestManager.getTest().log(Status.WARNING, message);

    }

    public void WARN(String message, Exception exception) {
        logger.warn(ConsoleColors.YELLOW + message + ConsoleColors.RESET, exception);
        ExtentTestManager.getTest().log(Status.WARNING, message);

    }

    public void ERROR(String message, Exception exception) {
        logger.error(ConsoleColors.RED + message + ConsoleColors.RESET, exception);
        ExtentTestManager.getTest().log(Status.WARNING, message);

    }

    public void ERROR(String message, Throwable exception) {
        logger.error(ConsoleColors.RED + message + ConsoleColors.RESET, exception);
        ExtentTestManager.getTest().log(Status.WARNING, exception);

    }

    public void FATAL(String message, Exception exception) {
        logger.fatal(ConsoleColors.RED_BACKGROUND + message + ConsoleColors.RESET, exception);
        ExtentTestManager.getTest().log(Status.FAIL, exception);

    }

    public void TRACE(String message, Exception exception) {
        logger.trace(ConsoleColors.PURPLE + message + ConsoleColors.RESET, exception);
        ExtentTestManager.getTest().log(Status.WARNING, message);

    }


    public void PASS(String message,ThreadLocal<ExtentTest> test) {
        test.get().pass("Test passed");
        ExtentTestManager.getTest().log(Status.PASS, message);

    }
    public void FAIL(ExceptionListner exceptionListener, ITestResult result, ThreadLocal<ExtentTest> test) {
        test.get().fail(exceptionListener.checkException(result.getThrowable().toString()));
        test.get().fail("details", MediaEntityBuilder.createScreenCaptureFromPath("1.png").build());
        ExtentTestManager.getTest().log(Status.FAIL, result.getMethod().getMethodName());

    }
    public void SKIP(ExceptionListner exceptionListener, ITestResult result, ThreadLocal<ExtentTest> test) {
        test.get().skip(exceptionListener.checkException(result.getThrowable().toString()));
        ExtentTestManager.getTest().log(Status.SKIP, result.getMethod().getMethodName());

    }


    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest = extent.createTest(result.getMethod().getDescription(),
                result.getMethod().getMethodName());
        test.set(extentTest);
        ExtentTestManager.startTest(result.getMethod().getDescription());
        logger.info("****************************************************************");
        logger.info("$$$$$$$$$$$$$$$$$$$$$ " + result.getMethod().getDescription() + "  $$$$$$$$$$$$$$$$$$$$$$$$$");
        logger.info("******************************************************************");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTestManager.getTest().log(Status.PASS, "Test passed");    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTestManager.getTest().log(Status.FAIL, "Test Failed on method :" + result.getThrowable() );

    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTestManager.getTest().log(Status.SKIP, "Test Skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("*** Test failed but within percentage % " + result.getMethod().getMethodName());
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("*** Test Suite " + context.getName() + " started ***");
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println(("*** Test Suite " + context.getName() + " ending ***"));
        ExtentTestManager.endTest();
        ExtentManager.getInstance().flush();
    }
}