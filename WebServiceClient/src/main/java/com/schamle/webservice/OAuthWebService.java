package com.schamle.webservice;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * Created by TYP9MXS on 2/14/2016.
 */
@Component
public class OAuthWebService {

    Logger log = Logger.getLogger(this.getClass());

    private Properties webServiceProperties;
    private String requestUrl;
    private String encodedUrl;
    private String authorizationString;

    public OAuthWebService() {}

    public OAuthWebService(Properties webServiceProperties) {
        this.webServiceProperties = webServiceProperties;
    }


    public WebServiceResponse get (String requestUrl) throws WebServiceException {
        this.requestUrl = requestUrl;
        initializeRequest(requestUrl);
        WebServiceResponse response = receive();
        return response;
    }

    private WebServiceResponse receive () throws WebServiceException{
        HttpURLConnection connection;
        WebServiceResponse response;
        try {
            connection = (HttpURLConnection) new URL(this.requestUrl).openConnection();
            connection.addRequestProperty("Authorization", authorizationString) ;
            connection.connect() ;
            response = resolveResponse(connection);
        }
        catch (IOException ex){
            log.warn("Communication Error connecting to URL: " + this.requestUrl, ex);
            throw new WebServiceException("Communication Error connecting to URL: " + this.requestUrl, ex);
        }
        return response;
    }

    private WebServiceResponse resolveResponse(HttpURLConnection connection) throws IOException{
        WebServiceResponse response = new WebServiceResponse();
        int responseCode = connection.getResponseCode();
        response.setResponseCode(responseCode);
        log.debug("Web Service Response Code is "+ responseCode);

        if (WebServiceResponse.RESPONSE_OK == responseCode ||
                WebServiceResponse.RESPONSE_NOT_FOUND == responseCode ||
                WebServiceResponse.RESPONSE_UNAUTHORIZED == responseCode){
            BufferedReader reader;
            if (WebServiceResponse.RESPONSE_OK == responseCode){ //normal
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }
            else { //error
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            log.debug("Web Service Response="+ sb.toString());
            response.setResponse(sb.toString());
        }

        return response;

    }

    private void initializeRequest(String requestUrl) throws WebServiceException{
        try {
            OAuthWebServiceRequest webServiceRequest = OAuthWebServiceRequestFactory.createRequest(webServiceProperties, requestUrl);
            encodedUrl = webServiceRequest.getEncodeURLAndParams();
            authorizationString = webServiceRequest.getAuthorizationString();
        }
        catch (UnsupportedEncodingException e){
            log.error("Configuration Error: Unsupported URL encoding", e);
            throw new WebServiceException("Configuration Error: Unsupported URL encoding", e);
        }
        catch (NoSuchAlgorithmException e){
            log.error("Configuration Error: No such MAC algorithm", e);
            throw new WebServiceException("Configuration Error: No such MAC algorithm", e);
        }
        catch (InvalidKeyException e){
            log.error("Configuration Error: Invalid Key", e);
            throw new WebServiceException("Configuration Error: Invalid Key", e);
        }
    }

    public Properties getWebServiceProperties() {
        return webServiceProperties;
    }

    public void setWebServiceProperties(Properties webServiceProperties) {
        this.webServiceProperties = webServiceProperties;
    }
}
