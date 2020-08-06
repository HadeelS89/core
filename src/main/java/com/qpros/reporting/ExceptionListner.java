package com.qpros.reporting;

public class ExceptionListner {

    public String checkException(String exception) {
        if (exception.contains("Assert")) {
            if(exception.indexOf("expected")!=-1) {
                exception = exception.substring(exception.indexOf("expected"), exception.lastIndexOf("]") + 1);
            }else if(exception.lastIndexOf("]")!=-1){
                exception= exception.substring(exception.lastIndexOf(":"),exception.lastIndexOf("]")+1);
            }else{
                exception=exception;
            }
        } else if (exception.contains("depends on not successfully")) {
            exception = "Skipped, since its depends on failed method";
        } else if (exception.contains("NoSuchElementException")) {
            exception = "Locator Not Exist";
        }
        return exception;

    }
}
