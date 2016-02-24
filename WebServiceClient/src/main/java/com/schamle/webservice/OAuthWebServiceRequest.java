package com.schamle.webservice;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 * Created by typ9mxs on 2/15/2016.
 */
public class OAuthWebServiceRequest {

    public static final String OAUTH_VERSION = "1.0";
    public static final String OAUTH_SIGNATURE_METHOD = "HMAC-SHA1";
    public static final String MAC_ALGORITHM = "HmacSHA1";

    public static final String ENCODING_FORMAT =  "UTF-8";
    public static final String METHOD_GET =  "GET&";
    public static final String AND =  "&";
    public static final String PAREN_BEGIN =  "\"";
    public static final String PAREN_END =  "\", ";

    public static final String TAG_OAUTH_CONSURMER_KEY = "oauth_consumer_key=";
    public static final String TAG_OAUTH_NONCE = "oauth_nonce=";
    public static final String TAG_SIGNATURE_METHOD = "oauth_signature_method=";
    public static final String TAG_SIGNATURE = "oauth_signature=";
    public static final String TAG_TIMESTAMP = "oauth_timestamp=";
    public static final String TAG_OAUTH_TOKEN = "oauth_token=";
    public static final String TAG_OAUTH_VERSION = "oauth_version=";
    public static final String TAG_OATH = "OAuth ";
    public static final String TAG_REALM = "realm=";

    private String baseUrl;
    private String appToken;
    private String accessToken;
    private String timestamp;
    private String nonce;
    private String appTokenSecret;
    private String accessTokenSecret;

    public OAuthWebServiceRequest(String baseUrl, String appToken, String accessToken, String appTokenSecret, String accessTokenSecret) {
        this.baseUrl = baseUrl;
        this.appToken = appToken;
        this.accessToken = accessToken;
        this.nonce = "" + System.currentTimeMillis();
        this.timestamp = "" + System.currentTimeMillis()/1000 ;
        this.appTokenSecret = appTokenSecret;
        this.accessTokenSecret = accessTokenSecret;
    }

    public String getEncodeURLAndParams() throws UnsupportedEncodingException{
        StringBuilder fullUrl = new StringBuilder();
        fullUrl.append(METHOD_GET).append(rawUrlEncode(baseUrl)).append(AND);
        StringBuilder params = new StringBuilder();
        params.append(TAG_OAUTH_CONSURMER_KEY).append(appToken).append(AND);
        params.append(TAG_OAUTH_NONCE).append(nonce).append(AND);
        params.append(TAG_SIGNATURE_METHOD).append(OAUTH_SIGNATURE_METHOD).append(AND);
        params.append(TAG_TIMESTAMP).append(timestamp).append(AND);
        params.append(TAG_OAUTH_TOKEN).append(accessToken).append(AND);
        params.append(TAG_OAUTH_VERSION).append(OAUTH_VERSION);
        String url = fullUrl.toString() + rawUrlEncode(params.toString());
        return url.toString();
    }
    public String getAuthorizationString() throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException{
        StringBuilder auth = new StringBuilder();
        auth.append(TAG_OATH);
        auth.append(TAG_REALM).append(PAREN_BEGIN).append(baseUrl).append(PAREN_END);
        auth.append(TAG_OAUTH_VERSION).append(PAREN_BEGIN).append(OAUTH_VERSION).append(PAREN_END);
        auth.append(TAG_TIMESTAMP).append(PAREN_BEGIN).append(timestamp).append(PAREN_END);
        auth.append(TAG_OAUTH_NONCE).append(PAREN_BEGIN).append(nonce).append(PAREN_END);
        auth.append(TAG_OAUTH_CONSURMER_KEY).append(PAREN_BEGIN).append(appToken).append(PAREN_END);
        auth.append(TAG_OAUTH_TOKEN).append(PAREN_BEGIN).append(accessToken).append(PAREN_END);
        auth.append(TAG_SIGNATURE_METHOD).append(PAREN_BEGIN).append(OAUTH_SIGNATURE_METHOD).append(PAREN_END);
        auth.append(TAG_SIGNATURE).append(PAREN_BEGIN).append(getDigestSignature()).append("\"");

        return auth.toString();
    }

    public String getSigningKey()  throws UnsupportedEncodingException{
        StringBuilder signingKey = new StringBuilder();
        signingKey.append(rawUrlEncode(appTokenSecret)).append(AND).append(rawUrlEncode(accessTokenSecret));
        return signingKey.toString();
    }

    private String getDigestSignature() throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException{
        String signingKey = getSigningKey();
        String urlAndParams = getEncodeURLAndParams();
        Mac mac = Mac.getInstance(MAC_ALGORITHM);
        SecretKeySpec secret = new SecretKeySpec(signingKey.getBytes(), mac.getAlgorithm());
        mac.init(secret);
        byte[] digest = mac.doFinal(urlAndParams.getBytes());
        String oauth_signature = DatatypeConverter.printBase64Binary(digest);
        return oauth_signature;
    }

    private String rawUrlEncode(String str) throws UnsupportedEncodingException {
        return URLEncoder.encode(str, ENCODING_FORMAT);
    }
}
