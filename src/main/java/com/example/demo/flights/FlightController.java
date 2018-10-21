package com.example.demo.flights;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class FlightController {

  @Autowired
  private FlightRepository flightRepository;
  @Autowired
  private FlightsService flightsService;

  @GetMapping("/flight")
  public Flight getFlightById(Long id) {
    return flightRepository.findById(id).get();
  }

  @GetMapping("/flights")
  public List<FoundFlight> getFlightsByRouteAndDate(@RequestParam("origin") Long origin,
                                               @RequestParam("destination") Long destination,
                                               @RequestParam("date")
                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    List<Flight> flights = flightsService.getFlightsByRouteAndDate(origin, destination, date);

    return flights.stream().map(f ->
        new FoundFlight(f.getId(),
            f.getFlightRouteTime().getFlightRoute().getOrigin(),
            f.getFlightRouteTime().getFlightRoute().getDestination(),
            LocalDateTime.of(f.getFlightDate(), f.getFlightRouteTime().getTime())
        )
    ).collect(Collectors.toList());
  }


}