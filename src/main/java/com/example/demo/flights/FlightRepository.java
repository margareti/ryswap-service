package com.example.demo.flights;

import com.example.demo.flights.route.FlightRoute;
import com.example.demo.flights.route.FlightRouteTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository <Flight, Long>  {

  @Query("from Flight f where f.flightDate = :flightDate and f.flightRouteTime in(:flightRouteTimes)")
  public List<Flight> findByFlightDateAndFlightRouteTime(
      @Param("flightDate") LocalDate localDate,
      @Param("flightRouteTimes") List<FlightRouteTime> flightRouteTime);
}
