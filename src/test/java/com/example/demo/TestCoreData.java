package com.example.demo;

import com.example.demo.flights.airport.Airport;
import com.example.demo.flights.airport.AirportRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * TODO: Javadoc
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public final class TestCoreData {

    @Autowired
    private TestDataSetup testDataSetup;
    @Autowired
    private AirportRepository airportRepository;

    @Test
    public void setUpAllCoreData() {
        testDataSetup.setUpAirports();
        System.out.println(airportRepository.count());
        testDataSetup.setUpRoutes();
    }




}