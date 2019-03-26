package com.example.demo.userflights;

import com.example.demo.flights.Flight;
import com.example.demo.flights.FlightRepository;
import com.example.demo.flights.seats.Seat;
import com.example.demo.flights.seats.SeatRepository;
import com.example.demo.flights.seats.SeatsConfiguration;
import com.example.demo.flights.seats.SeatsConfigurationRepository;
import com.example.demo.userflights.swaprequest.SwapRequestRepository;
import com.example.demo.users.User;
import com.example.demo.users.login.UserLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class FlightSeatController {
  @Autowired
  private SeatsConfigurationRepository seatsConfigurationRepository;

  @Autowired
  private UserFlightRepository userFlightRepository;

  @Autowired
  private FlightRepository flightRepository;

  @Autowired
  private FlightSeatRepository flightSeatRepository;

  @Autowired
  private UserLoginRepository userLoginRepository;

  @Autowired
  private SwapRequestRepository swapRequestRepository;

  @Autowired
  private SeatRepository seatRepository;

  @PostMapping("/flight/{flightId}/my-seats")
  public void  addMySeats(
      @PathVariable("flightId") Long flightId,
      @RequestBody List<Long> seatIds,
      @Autowired Principal principal) {
      User user = userLoginRepository.findByUsername(principal.getName()).get().getUser();
      Flight flight = flightRepository.findById(flightId).get();

      List<FlightSeat> currentSeats = flightSeatRepository.findByOwnerAndFlight(user, flight);
      currentSeats.stream().forEach(seat -> {
        if (!seatIds.contains(seat.getId())) {
          seat.setOwner(null);
          flightSeatRepository.save(seat);
        }
      });

      seatIds
          .stream()
          .forEach(seatId -> {
            Seat seat = seatRepository.getOne(seatId);
            FlightSeat flightSeat = flightSeatRepository.findByFlightAndSeat(flight, seat);
            if (flightSeat == null) {
              flightSeat = FlightSeat.builder().seat(seat).flight(flight).owner(user).build();
            } else {
              if (flightSeat.getOwner() == null) {
                flightSeat.setOwner(user);
              } else if (!flightSeat.getOwner().equals(user)) {
                throw new RuntimeException("This seat already belongs to someone else");
              }

            }
            flightSeatRepository.save(flightSeat);
          });
  }

  @GetMapping("/flight/{flightId}/my-seats")
  public List<FlightSeatData> getMySeats(@PathVariable("flightId") Long flightId,
                                             @Autowired Principal principal) {

    Flight flight = flightRepository.findById(flightId).get();
    User user = userLoginRepository.findByUsername(principal.getName()).get().getUser();

    return flightSeatRepository.findByOwnerAndFlight(user, flight)
        .stream()
        .map(seat -> new FlightSeatData()
            .builder()
            .belongsToUser(true)
            .isOccupied(true)
            .seat(seat.getSeat()).build()
        )
        .collect(Collectors.toList());
  }

  @GetMapping("/flight/{flightId}/seats")
  public List<FlightSeatData> getFlightSeats(@PathVariable("flightId") Long flightId,
                                             @Autowired Principal principal) {

    Flight flight = flightRepository.findById(flightId).get();
    List <FlightSeat> flightSeats = flightSeatRepository.findByFlight(flight);
    User user = userLoginRepository.findByUsername(principal.getName()).get().getUser();
    SeatsConfiguration seatsConfiguration = seatsConfigurationRepository.findByFlightId(flightId).get();

    return seatsConfiguration.getSeats().stream().map(s-> {
      Optional<FlightSeat> seat = flightSeats.stream().filter(fs -> fs.getSeat().getId().equals(s.getId())).findFirst();
      return new FlightSeatData().builder()
          .seat(s)
          .isOccupied(seat.isPresent() && seat.get().getOwner() != null)
          .belongsToUser(seat.isPresent() && user.equals(seat.get().getOwner())).build();
        }
    ).collect(Collectors.toList());
  }
}
