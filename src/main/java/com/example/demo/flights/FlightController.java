package com.example.demo.flights;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

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
  public List<Flight> getFlightsByRouteAndDate(@RequestParam("routeId") Long routeId,
                                               @RequestParam("date")
                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    return flightsService.getFlightsByRouteAndDate(routeId, date);
  }


//
//  TODO save flightId to user. at the point of saving flight, available flights for
//  certain date should have IDs.
//  so, in effect. this will be "get flight by id" and save flight to user"
//  @GetMapping("/get-flight-by-datetime-and-route")
//  public Flight getFlightByDatetimeAndRoute(LocalDateTime datetime, Long routeId) {
//    return flightRepository.f
//  }
//
//  @PostMapping("/add-flight")
//  public Flight addFlight(Flight flight) {
//    return flightRepository
//  }
}