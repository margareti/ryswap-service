package com.example.demo.userflights;

import com.example.demo.flights.Flight;
import com.example.demo.flights.FlightRepository;
import com.example.demo.flights.FoundFlight;
import com.example.demo.flights.seats.Seat;
import com.example.demo.userflights.swaprequest.SwapRequestRepository;
import com.example.demo.users.User;
import com.example.demo.users.UserRepository;
import com.example.demo.users.login.UserLoginRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserFlightController {

  private static final Logger logger = LoggerFactory.getLogger(UserFlightController.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserLoginRepository userLoginRepository;

  @Autowired
  private FlightRepository flightRepository;

  @Autowired
  private UserFlightRepository userFlightRepository;

  @Autowired
  private SwapRequestRepository swapRequestRepository;

  @Autowired
  private FlightSeatRepository flightSeatRepository;



  @PostMapping("/user/flights/{flightId}")
  public ResponseEntity<MyFlightResponse> addFlightToUser(@Autowired Principal principal, @PathVariable("flightId") Long id) {

    User user = userLoginRepository.findByUsername(principal.getName()).get().getUser();
    Flight flight = flightRepository.findById(id).get();

    if (userFlightRepository.findByUserAndFlight(user, flight) == null) {
      UserFlight userFlight = new UserFlight();
      userFlight.setUser(user);
      userFlight.setFlight(flight);
      userFlightRepository.save(userFlight);
      return ResponseEntity.ok(createMyFlightResponse(userFlight));
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
  }

  @GetMapping("/user/flights")
  public List<MyFlightResponse> getUserFlights(@Autowired Principal principal) {
    logger.info("start get user flights");
    User user = userLoginRepository.findByUsername(principal.getName()).get().getUser();
    logger.info("user found");
    List<MyFlightResponse> myFlights = userFlightRepository
        .findByUser(user)
        .stream()
        .map(uf -> createMyFlightResponse(uf))
        .collect(Collectors.toList());

    logger.info("get user flights end");
    return myFlights;
  }

  private MyFlightResponse createMyFlightResponse(UserFlight uf) {
    FoundFlight foundFlight = new FoundFlight(uf.getFlight().getId(),
        uf.getFlight().getFlightRouteTime().getFlightRoute().getOrigin(),
        uf.getFlight().getFlightRouteTime().getFlightRoute().getDestination(),
        LocalDateTime.of(uf.getFlight().getFlightDate(), uf.getFlight().getFlightRouteTime().getTime()));

    List<Seat> mySeats = flightSeatRepository.findByOwnerAndFlight(uf.getUser(), uf.getFlight())
        .stream()
        .map(seat -> seat.getSeat())
        .collect(Collectors.toList());
    return new MyFlightResponse(uf.getFlight().getId(), foundFlight, mySeats);
  }

}
