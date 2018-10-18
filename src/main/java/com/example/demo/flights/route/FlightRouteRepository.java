package com.example.demo.flights.route;

import com.example.demo.flights.airport.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FlightRouteRepository extends JpaRepository<FlightRoute, Long> {

  Optional<FlightRoute> findByOrigin(Airport origin);
  Optional<FlightRoute> findByDestination(Airport destination);

}
