package com.schamle.webservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by Marc & Trilce on 2/13/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-config.xml"})
public class WebServiceClientTest {

    @Autowired
    OAuthWebService webService;

    @Test
    public void test() throws Exception {
        WebServiceResponse response = webService.get("https://www.mkmapi.eu/ws/v1.1/games");
        assertNotNull(response);
    }
}
