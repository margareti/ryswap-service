package com.example.demo.flights.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FlightRoutesController {

  @Autowired private FlightRouteRepository flightRouteRepository;

  @GetMapping("/routes")
  public List<FlightRoute> getAllFlightRoutes() {
    return flightRouteRepository.findAll();
  }






}
