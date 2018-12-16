package com.example.demo.userflights.swaprequest;

import com.example.demo.flights.Flight;
import com.example.demo.flights.seats.Seat;
import com.example.demo.userflights.UserFlight;
import com.example.demo.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SwapRequestRepository extends JpaRepository<SwapRequest, Long> {

    @Query("from SwapRequest sr where sr.flight.id = :flightId ")
    List<SwapRequest> findByFlightId(@Param("flightId") Long flightId);


    List<SwapRequest> findByFlightAndAuthor(Flight flight, User author);

    List<SwapRequest> findByFlightAndAuthorAndSwapRequestStatus(Flight flight,
                                                                User author,
                                                                SwapRequestStatus swapRequestStatus);

    List<SwapRequest> findByFlightAndSwapRequestStatus(Flight flight, SwapRequestStatus swapRequestStatus);
    @Query("from SwapRequest sr where sr.flight.id = :flightId and sr.targetSeat in (:targetSeats)")
    List<SwapRequest> findByFlightAndTargetSeats(@Param("flightId") Long flightId,
        @Param("targetSeats") List<Seat> targetSeat);

}
