package com.example.demo.userflights;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SwapRequestRepository extends JpaRepository<SwapRequest, Long> {

    @Query("from SwapRequest sr where sr.userFlight.flight.id = :flightId ")
    List<SwapRequest> findByFlightId(@Param("flightId") Long flightId);

    List<SwapRequest> findByUserFlight(UserFlight userFlight);

}
