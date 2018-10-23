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

  public List<Flight> getFlightsByRouteAndDate(Long origin, Long destination, LocalDate date) {
    FlightRoute route = flightRouteRepository.findByOriginAndDestination(origin, destination).get();
    DayOfWeek dayOfWeek = date.getDayOfWeek();

    List<FlightRouteTime> routeTimes = route
        .getFlightRouteTimes().stream()
        .filter(f -> f.getDayOfWeek() == dayOfWeek).collect(Collectors.toList());


    List<Flight> existingFlights = flightRepository.findByFlightDateAndFlightRouteTime(date, routeTimes);

    List<Flight> newFlights = routeTimes.stream()
        .filter(r -> existingFlights.stream().noneMatch(f -> f.getFlightRouteTime().equals(r)))
        .map(frt -> createFlight(frt, date))
        .collect(Collectors.toList());


    flightRepository.saveAll(newFlights);

    existingFlights.addAll(newFlights);

    return existingFlights;

  }


   private Flight createFlight(FlightRouteTime flightRouteTime, LocalDate date){
     Flight flight = new Flight();
     flight.setFlightRouteTime(flightRouteTime);
     flight.setFlightDate(date);
     return  flight;
  }
}
