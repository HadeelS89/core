package com.qpros.common;

public class OsValidator {

    private static String OS = System.getProperty("os.name").toLowerCase();

    public static String  getDeviceOs(){
        if (isWindows()) {
            return "windows";
        } else if (isMac()) {
            return "mac";
        } else if (isUnix()) {
            return "Unix";
        } else {
            return "invalid";
        }
    }

    public static boolean isWindows() {

        return (OS.indexOf("win") >= 0);
    }

    public static boolean isMac() {

        return (OS.indexOf("mac") >= 0);
    }

    public static boolean isUnix() {

        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );

    }
}
