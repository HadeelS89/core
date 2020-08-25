package com.qpros.common;

import com.aventstack.extentreports.ExtentTest;
import com.qpros.reporting.ExceptionListner;
import com.qpros.utils.ConsoleColors;
import org.apache.log4j.Logger;
import org.testng.ITestResult;

public class LogManger {

    static Logger logger;


    public LogManger(String classObjName) {
        logger = Logger.getLogger(classObjName);
    }

    public void INFO(String message) {
        logger.info(ConsoleColors.BLUE + message + ConsoleColors.RESET);
    }

    public void INFO(Integer message) {
        logger.info(ConsoleColors.BLUE + message + ConsoleColors.RESET);
    }

    public void DEBUG(String message) {
        logger.debug(ConsoleColors.GREEN + message + ConsoleColors.RESET);
    }

    public void WARN(String message) {
        logger.warn(ConsoleColors.YELLOW + message + ConsoleColors.RESET);
    }

    public void ERROR(String message) {
        logger.info(ConsoleColors.RED + message + ConsoleColors.RESET);
    }

    public void FATAL(String message) {
        logger.info(ConsoleColors.RED_BACKGROUND + message + ConsoleColors.RESET);
    }

    public void TRACE(String message) {
        logger.trace(ConsoleColors.PURPLE + message + ConsoleColors.RESET);
    }

    public void INFO(String message, Exception exception) {
        logger.info(ConsoleColors.BLUE + message + ConsoleColors.RESET, exception);
    }

    public void DEBUG(String message, Exception exception) {
        logger.debug(ConsoleColors.GREEN + message + ConsoleColors.RESET, exception);
    }

    public void WARN(String message, Exception exception) {
        logger.warn(ConsoleColors.YELLOW + message + ConsoleColors.RESET, exception);
    }

    public void ERROR(String message, Exception exception) {
        logger.error(ConsoleColors.RED + message + ConsoleColors.RESET, exception);
    }

    public void ERROR(String message, Throwable exception) {
        logger.error(ConsoleColors.RED + message + ConsoleColors.RESET, exception);
    }

    public void FATAL(String message, Exception exception) {
        logger.fatal(ConsoleColors.RED_BACKGROUND + message + ConsoleColors.RESET, exception);
    }

    public void TRACE(String message, Exception exception) {
        logger.trace(ConsoleColors.PURPLE + message + ConsoleColors.RESET, exception);
    }


    public void PASS(String Message,ThreadLocal<ExtentTest> test) {
        test.get().pass("Test passed");
    }
    public void FAIL(ExceptionListner exceptionListener, ITestResult result, ThreadLocal<ExtentTest> test) {
        test.get().fail(exceptionListener.checkException(result.getThrowable().toString()));
    }
    public void SKIP(ExceptionListner exceptionListener, ITestResult result, ThreadLocal<ExtentTest> test) {
        test.get().skip(exceptionListener.checkException(result.getThrowable().toString()));
    }


}