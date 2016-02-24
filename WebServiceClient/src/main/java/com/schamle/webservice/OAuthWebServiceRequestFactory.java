package com.schamle.webservice;

import java.util.Properties;

/**
 * Created by typ9mxs on 2/16/2016.
 */
public class OAuthWebServiceRequestFactory {
    public static final String APP_TOKEN_NAME = "appToken";
    public static final String APP_SECRET_NAME =  "appSecret";
    public static final String ACCESS_TOKEN_NAME = "accessToken";
    public static final String ACCESS_TOKEN_SECRET_NAME = "accessTokenSecret";

    public static OAuthWebServiceRequest createRequest (Properties webServiceProperties, String requestUrl){
        String appToken = webServiceProperties.getProperty(APP_TOKEN_NAME);
        String appSecret = webServiceProperties.getProperty(APP_SECRET_NAME);
        String accessToken = webServiceProperties.getProperty(ACCESS_TOKEN_NAME);
        String accessTokenSecret = webServiceProperties.getProperty(ACCESS_TOKEN_SECRET_NAME);
        return new OAuthWebServiceRequest(requestUrl,appToken,accessToken,appSecret,accessTokenSecret);
    }
}
