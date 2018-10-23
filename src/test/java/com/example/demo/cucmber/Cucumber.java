package com.example.demo.cucmber;

import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(cucumber.api.junit.Cucumber.class )
@CucumberOptions( features = {"src/test/resources/features"})
public final class Cucumber {
}