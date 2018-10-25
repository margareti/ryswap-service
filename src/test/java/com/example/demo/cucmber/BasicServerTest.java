package com.example.demo.cucmber;


import com.example.demo.TestDataSetup;
import com.example.demo.users.register.RegisterUserRequest;
import cucumber.api.java.Before;
import cucumber.api.java.bs.A;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;

@Ignore
//@Component
public  class BasicServerTest  extends  SpringBootBaseIntegrationTest {

    private String infoResult;
    private int httpResponseCode;
    private Object request;

    @Autowired
    HttpClient integrationTest;

    @Autowired
    private TestDataSetup testDataSetup;


    @Before
    public void before() {
       // testDataSetup.getUserRole();
    }



    @When("^I ask if the server is up$")
    public void checkServerUp() {
      this.infoResult = integrationTest.withEndpoint("/actuator/health").getSomething(String.class);
    }

    @Then("the server is up")
    public void isTheServerUp() {
        assertNotNull(infoResult);
    }

    @Given("a sign up request for \"([^\"]*)\" with password \"([^\"]*)\" and name \"([^\"]*)\"")
    public void a_sign_up_request_for_with_password_and_name(String userEmail, String password, String name) {
        this.request = new RegisterUserRequest(name, userEmail, password);
    }

    @When("I post the request to \"([^\"]*)\"")
    public void i_send_a_register_user_request(String endpoint) {
        httpResponseCode = integrationTest.withEndpoint("/api/registerUser").put((RegisterUserRequest)request);
    }

    @Then("the server responds with \"([^\"]*)\"")
    public void the_server_responds_with(String response) {
        assertEquals(httpResponseCode, Integer.parseInt(response));
    }


}