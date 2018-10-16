package com.example.demo.flights;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FlightRepository extends JpaRepository <Flight, Long>  {
  public List<Flight> findByFlightDateAndFlightRouteTime(LocalDate localDate, List<FlightRouteTime> flightRouteTime);

}
