package com.example.demo.cucmber;

import java.util.List;
import java.util.Objects;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class SpringBootBaseIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(SpringBootBaseIntegrationTest.class);

    private final String SERVER_URL = "http://localhost";
    private final String THINGS_ENDPOINT = "/things";

    private String endpoint;


    private RestTemplate restTemplate;

    @LocalServerPort
    protected int port;



    public SpringBootBaseIntegrationTest withEndpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }


    public SpringBootBaseIntegrationTest() {
        restTemplate = new RestTemplate();
    }

    private String thingsEndpoint() {
        return SERVER_URL + ":" + port + endpoint;
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