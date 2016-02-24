package com.schamle.webservice;

/**
 * Created by typ9mxs on 2/16/2016.
 */
public class WebServiceException extends Exception {

    public WebServiceException(Throwable exception){
        super(exception);
    }

    public WebServiceException(String message, Throwable exception){
        super(message, exception);
    }
}
