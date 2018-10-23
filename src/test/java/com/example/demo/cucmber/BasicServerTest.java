package com.example.demo.cucmber;


import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.junit.Assert.assertNotNull;
import org.junit.Ignore;

@Ignore
public final class BasicServerTest extends  SpringBootBaseIntegrationTest{

    private String infoResult;

    @When("^I ask is the servier up?$")
    public void checkServerUp() {
      this.infoResult =   withEndpoint("/info").getSomething(String.class);
    }

    @Then("the servier is up")
    public void isTheServerUp() {
        assertNotNull(infoResult);
    }

}