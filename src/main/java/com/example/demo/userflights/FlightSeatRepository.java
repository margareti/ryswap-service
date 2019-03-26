package com.example.demo.userflights;

import com.example.demo.flights.Flight;
import com.example.demo.flights.seats.Seat;
import com.example.demo.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlightSeatRepository extends JpaRepository  <FlightSeat, Long>{

  public List<FlightSeat> findByFlight(Flight flight);
  public List<FlightSeat> findByOwnerAndFlight(User owner, Flight flight);
  public FlightSeat findByFlightAndSeat(Flight flight, Seat seat);
}
