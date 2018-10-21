package com.example.demo.flights.route;

import com.example.demo.flights.airport.Airport;
import com.example.demo.users.login.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FlightRouteRepository extends JpaRepository<FlightRoute, Long> {

  Optional<FlightRoute> findByOrigin(Airport origin);
  Optional<FlightRoute> findByDestination(Airport destination);

  @Query("from FlightRoute fr where fr.origin.id = :originId and fr.destination.id = :destinationId")
  Optional<FlightRoute> findByOriginAndDestination(@Param("originId") Long originId, @Param("destinationId") Long destinationId);

}
