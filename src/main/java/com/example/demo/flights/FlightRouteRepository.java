package com.example.demo.flights;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FlightRouteRepository extends JpaRepository<FlightRoute, Long> {

  Optional<FlightRoute> findByOrigin(Airport origin);
  Optional<FlightRoute> findByDestination(Airport destination);

}
