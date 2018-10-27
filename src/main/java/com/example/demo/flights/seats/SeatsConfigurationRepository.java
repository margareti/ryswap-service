package com.example.demo.flights.seats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SeatsConfigurationRepository extends JpaRepository <SeatsConfiguration, Long> {

        @Query("select f.flightRouteTime.seatsConfiguration from Flight f where f.id = :flightId ")
        Optional<SeatsConfiguration> findByFlightId(@Param("flightId") Long flightId);
}

