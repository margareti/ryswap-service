package com.example.demo.userflights;

import com.example.demo.flights.Flight;
import com.example.demo.flights.FlightRepository;
import com.example.demo.users.User;
import com.example.demo.users.UserRepository;
import com.example.demo.users.login.UserLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.security.Principal;

@RestController
@RequestMapping("/api")
public class UserFlightController {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserLoginRepository userLoginRepository;

  @Autowired
  private FlightRepository flightRepository;

  @Autowired
  private UserFlightRepository userFlightRepository;



  @PostMapping("/user/flights/{flightId}")
  public UserFlight addFlightToUser(@Autowired Principal principal, @PathVariable("flightId") Long id) {

    User user = userLoginRepository.findByUsername(principal.getName()).get().getUser();
    Flight flight = flightRepository.findById(id).get();

    UserFlight userFlight = new UserFlight();
    userFlight.setUser(user);
    userFlight.setFlight(flight);
    return userFlightRepository.save(userFlight);
  }

}
