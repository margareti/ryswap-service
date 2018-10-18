package com.example.demo.flights;

import com.example.demo.flights.route.FlightRoute;
import com.example.demo.flights.route.FlightRouteRepository;
import com.example.demo.flights.route.FlightRouteTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightsService {

  @Autowired private FlightRepository flightRepository;
  @Autowired private FlightRouteRepository flightRouteRepository;

  public List<Flight> getFlightsByRouteAndDate(Long routeId, LocalDate date) {
    FlightRoute route = flightRouteRepository.findById(routeId).get();
    // {id: 2, origin: AirportObj, dest: AirportObj, flightRouteTime: [{id: 2, flightRoute: this, dayOfWeek: 2, time: 22h30}]}
    DayOfWeek dayOfWeek = date.getDayOfWeek(); //2

    List<FlightRouteTime> routeTimes = route
        .getFlightRouteTimes().stream()
        .filter(f -> f.getDayOfWeek() == dayOfWeek).collect(Collectors.toList());

    //[{id: 2, flightRoute: {}, dayOfWeek: 2, time: 10h30}, {id: 2, flightRoute: {}, dayOfWeek: 2, time: 22h30}]

//    routeTimes.forEach(route -> flightRepository.createFlight());
//
    List<Flight> existingFlights = flightRepository.findByFlightDateAndFlightRouteTime(date, routeTimes);
    //[{id: 3, flightRouteTime: {flightRoute: {}, ...}, datetime: 31/08/18 22h30}]

    List<Flight> newFlights = routeTimes.stream()
        .filter(r -> existingFlights.stream().noneMatch(f -> f.getFlightRouteTime().equals(r)))
        .map(frt -> createFligt(frt, date))
        .collect(Collectors.toList());


    flightRepository.saveAll(newFlights);

    existingFlights.addAll(newFlights);

    return existingFlights;

  }


   private Flight createFligt(FlightRouteTime flightRouteTime, LocalDate date){
     Flight flight = new Flight();
     flight.setFlightRouteTime(flightRouteTime);
     flight.setFlightDate(date);
     return  flight;
  }
}
