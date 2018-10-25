package com.example.demo.cucmber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * TODO: Javadoc
 */
@Component
public final class HttpClient {

    private static final Logger log = LoggerFactory.getLogger(SpringBootBaseIntegrationTest.class);

    private final String SERVER_URL = "http://localhost";
    private String endpoint;


    private RestTemplate restTemplate;
    @Autowired
    private Environment environment;

    public HttpClient withEndpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }


    public HttpClient() {
        restTemplate = new RestTemplate();
    }

    private String thingsEndpoint() {
        return SERVER_URL + ":" + environment.getProperty("local.server.port") + endpoint;
    }

    int put(final Object something) {
        return restTemplate.postForEntity(thingsEndpoint(), something, Void.class).getStatusCodeValue();
    }

    <T> T getSomething(Class<T> clazz) {
        return restTemplate.getForEntity(thingsEndpoint(), clazz).getBody();
    }

    void clean() {
        restTemplate.delete(thingsEndpoint());
    }
}