package com.schamle.webservice;

/**
 * Created by typ9mxs on 2/15/2016.
 */
public class WebServiceResponse {
    public static final int RESPONSE_OK = 200;
    public static final int RESPONSE_PARTIAL = 206;
    public static final int RESPONSE_NO_CONTENT = 204;
    public static final int RESPONSE_BAD_REQUEST = 400;
    public static final int RESPONSE_UNAUTHORIZED = 401;
    public static final int RESPONSE_FORBIDDEN = 403;
    public static final int RESPONSE_NOT_FOUND = 404;
    public static final int RESPONSE_NOT_ALLOWED = 405;
    public static final int RESPONSE_PRECONDITION_FAILED = 412;
    public static final int RESPONSE_EXPECTATION_FAILED = 417;

    private int responseCode;
    private String response;
    private Throwable exception;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }
}
