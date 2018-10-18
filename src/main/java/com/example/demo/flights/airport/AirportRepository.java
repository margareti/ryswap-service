package com.example.demo.flights.airport;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AirportRepository extends JpaRepository<Airport, Long> {

  Optional<Airport> findByAirportCode(String code);
  Optional<Airport> findByAirportName(String name);

}
