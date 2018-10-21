package com.example.demo.userflights;

import com.example.demo.flights.Flight;
import com.example.demo.flights.FlightRepository;
import com.example.demo.flights.FoundFlight;
import com.example.demo.users.User;
import com.example.demo.users.UserRepository;
import com.example.demo.users.login.UserLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
  public void addFlightToUser(@Autowired Principal principal, @PathVariable("flightId") Long id) {

    User user = userLoginRepository.findByUsername(principal.getName()).get().getUser();
    Flight flight = flightRepository.findById(id).get();

    if (userFlightRepository.findByUserAndFlight(user, flight) != null) {
      UserFlight userFlight = new UserFlight();
      userFlight.setUser(user);
      userFlight.setFlight(flight);
      userFlightRepository.save(userFlight);
    }
  }

  @GetMapping("/user/flights")
  public List<FoundFlight> getUserFlights(@Autowired Principal principal) {
    User user = userLoginRepository.findByUsername(principal.getName()).get().getUser();
    return userFlightRepository
        .findByUser(user).stream()
        .map(uf -> new FoundFlight(uf.getFlight().getId(),
            uf.getFlight().getFlightRouteTime().getFlightRoute().getOrigin(),
            uf.getFlight().getFlightRouteTime().getFlightRoute().getDestination(),
            LocalDateTime.of(uf.getFlight().getFlightDate(), uf.getFlight().getFlightRouteTime().getTime())))
        .collect(Collectors.toList());
  }

}
