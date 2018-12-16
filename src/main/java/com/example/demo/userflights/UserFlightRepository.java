package com.example.demo.userflights;

import com.example.demo.flights.Flight;
import com.example.demo.users.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserFlightRepository extends JpaRepository <UserFlight, Long>{
  UserFlight findByUserAndFlight(User user, Flight flight);

  @EntityGraph(attributePaths = {"flight"})
  List<UserFlight> findByUser(User user);
}
