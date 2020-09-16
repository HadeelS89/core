package com.qpros.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.qpros.common.Base;
import com.qpros.helpers.ActionsHelper;

public class ExtentManager extends Base {

    private static ExtentReports extent;
    private static String reportClassName;
    public static String reportFileName = String.format("QPros-Automation_Report-%s-%s.html",
            ActionsHelper.getTodayDate(),System.currentTimeMillis());
    public static String path = System.getProperty("user.dir") + "/src/main/resources/Reports/";

    public static ExtentReports getInstance() {
        if (extent == null)
            createInstance();
        return extent;
    }

    public static ExtentReports createInstance() {
        ExtentSparkReporter extentSparkReporter= new ExtentSparkReporter(path + reportFileName);
        StateHelper.setStepState("reportName", reportFileName);
        //htmlReporter.config().setTestViewChartLocation( ChartLocation.TOP);
        //htmlReporter.config().setChartVisibilityOnOpen(true);
        extentSparkReporter.config().setTheme( Theme.STANDARD);
        extentSparkReporter.config().setEncoding("utf-8");



        extent = new ExtentReports();
        extent.attachReporter(extentSparkReporter);



        return extent;
    }

}
