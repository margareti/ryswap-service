package com.example.demo.flights;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AirportController {

  @Autowired private AirportRepository airportRepository;

  @GetMapping("/airports")
  public List<Airport> getAllAirports(){
    return airportRepository.findAll();
  }


}
