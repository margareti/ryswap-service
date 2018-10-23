package com.example.demo;

import com.example.demo.flights.FlightRepository;
import com.example.demo.flights.airport.Airport;
import com.example.demo.flights.airport.AirportRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static  Logger logger = LoggerFactory.getLogger(TestCoreData.class);



    @Autowired
    private TestDataSetup testDataSetup;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Test
    public void setUpAllCoreData() {
        testDataSetup.setUpFlightsCoreData();
        logger.info(flightRepository.findAll().toString());
    }




}