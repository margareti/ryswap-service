package com.example.demo;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.example.demo.flights.airport.Airport;
import com.example.demo.flights.airport.AirportRepository;
import com.example.demo.flights.route.FlightRoute;
import com.example.demo.flights.route.FlightRouteRepository;
import com.example.demo.flights.route.FlightRouteTime;
import com.example.demo.flights.route.FlightRouteTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class TestDataSetup {

    @Autowired
    private AirportRepository airportRepository;
    @Autowired
    private FlightRouteRepository flightRouteRepository;

    @Autowired
    private FlightRouteTimeRepository flightRouteTimeRepository;

    public void setUpAirports() {
        createAirport("BCN", "Barcelona International Airport");
        createAirport("CDG", "Charles de Gaulle Airport");
        createAirport("ATL", "Hartsfield–Jackson Atlanta International Airport");
        createAirport("PEK", "Beijing Capital International Airport");
        createAirport("SVO", "Sheremetyevo International Airport");
        createAirport("SOF", "Sofia International Airport");
        createAirport("EDI", "Edinburgh International Airport");
    }

    private void createAirport(String airportCode, String airportName) {
        Airport airport = new Airport();
        airport.setAirportCode(airportCode);
        airport.setAirportName(airportName);
        airportRepository.save(airport);
    }

    public void setUpRoutes() {
        FlightRoute fr = createFligthRoute("SOF", "EDI");
        List<FlightRouteTime> flightRouteTimes = createTimes(fr, DayOfWeek.MONDAY, LocalTime.of(12, 0), LocalTime.of(21, 30) );
        flightRouteTimes.addAll(createTimes(fr, DayOfWeek.MONDAY, LocalTime.of(13, 30), LocalTime.of(18, 30) ));
        fr.setFlightRouteTimes(flightRouteTimes);
        flightRouteRepository.save(fr);
        System.out.println(fr);
    }

    private FlightRoute createFligthRoute(String origCode, String destCode) {
        FlightRoute fr = new FlightRoute();
        fr.setOrigin(airportRepository.findByAirportCode(origCode).get());
        fr.setDestination(airportRepository.findByAirportCode(destCode).get());
        return flightRouteRepository.save(fr);
    }

    private List<FlightRouteTime> createTimes(FlightRoute fr, DayOfWeek dayOfWeek, LocalTime... times) {
        return Stream.of(times).map(t -> {FlightRouteTime frt = new FlightRouteTime();
        frt.setDayOfWeek(dayOfWeek); frt.setTime(t); frt.setFlightRoute(fr); return frt;}).map(frt -> flightRouteTimeRepository.save(frt) ).collect(Collectors.toList());
    }



}