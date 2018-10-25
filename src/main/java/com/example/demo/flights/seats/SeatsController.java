package com.example.demo.flights.seats;

import com.example.demo.flights.Flight;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SeatsController {

    private SeatsConfigurationRepository seatsConfigurationRepository;

    @GetMapping("/seatsConfiguration")
    public SeatsConfiguration getSeatsConfiguration(Long flightId) {
        return seatsConfigurationRepository.findByFlightId(flightId).get();
    }


}
