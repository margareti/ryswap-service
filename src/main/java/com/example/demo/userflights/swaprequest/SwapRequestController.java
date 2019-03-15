package com.example.demo.userflights.swaprequest;

import com.example.demo.flights.Flight;
import com.example.demo.flights.FlightRepository;
import com.example.demo.flights.seats.Seat;
import com.example.demo.flights.seats.SeatRepository;
import com.example.demo.flights.seats.SeatsConfiguration;
import com.example.demo.flights.seats.SeatsConfigurationRepository;
import com.example.demo.userflights.*;
import com.example.demo.users.User;
import com.example.demo.users.login.UserLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/api")
public class SwapRequestController {

    @Autowired
    private SeatsConfigurationRepository seatsConfigurationRepository;

    @Autowired
    private UserFlightRepository userFlightRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private UserLoginRepository userLoginRepository;

    @Autowired
    private SwapRequestRepository swapRequestRepository;

    @Autowired
    private SeatRepository seatRepository;

    @GetMapping("/flight/{flightId}/seats")
    public List<FlightSeat> getFlightSeats(@PathVariable("flightId") Long flightId,
                                           @Autowired Principal principal) {

        Flight flight = flightRepository.findById(flightId).get();
        List<SwapRequest> swapRequests = swapRequestRepository.findByFlightAndSwapRequestStatus(flight, SwapRequestStatus.ACCEPTED);
        User user = userLoginRepository.findByUsername(principal.getName()).get().getUser();
        SeatsConfiguration seatsConfiguration = seatsConfigurationRepository.findByFlightId(flightId).get();

        return seatsConfiguration.getSeats().stream().map(s-> new FlightSeat().builder()
                .seat(s)
                .isOccupied(swapRequests.stream().filter(sw -> {
                    return sw.getTargetSeat().equals(s);
                }).findFirst().isPresent())
                .belongsToUser(swapRequests.stream()
                    .filter(sw -> sw.getTargetSeat().equals(s))
                    .filter(sw -> sw.getAuthor().equals(user)).findFirst().isPresent()
                ).build()
        ).collect(Collectors.toList());
    }

    @PostMapping("/flight/{flightId}/my-seats")
    public void  addMySeats(
        @PathVariable("flightId") Long flightId,
        @RequestBody List<Long> seatIds,
        @Autowired Principal principal) {
        User user = userLoginRepository.findByUsername(principal.getName()).get().getUser();
        Flight flight = flightRepository.findById(flightId).get();
        List<SwapRequest> userSwapRequests = swapRequestRepository
            .findByFlightAndAuthorAndSwapRequestStatus(flight,user, SwapRequestStatus.ACCEPTED);
        userSwapRequests
            .stream()
            .filter(sr -> !seatIds.stream()
                .filter(si -> sr.getTargetSeat().getId().equals(si)).findFirst().isPresent())
            .peek(sr -> swapRequestRepository.delete(sr)).toArray();

          seatIds
            .stream()
            .filter( sid -> !userSwapRequests.stream()
                .filter(sr -> sr.getTargetSeat().getId().equals(sid)).findFirst().isPresent())
            .map(si -> new SwapRequest().builder()
                .flight(flight)
                .currentSeat(null)
                .targetSeat(seatRepository.findById(si).get())
                .swapRequestStatus(SwapRequestStatus.ACCEPTED)
                .author(user).build())
            .peek(swapRequest -> swapRequestRepository.save(swapRequest)).collect(Collectors.toList());



    }

    @PostMapping("/flight/{flightId}/swap-request")
    public ResponseEntity makeSwapRequest(
        @PathVariable("flightId") Long flightId,
        @RequestBody SwapRequestRequest swapRequestRequest,
        @Autowired Principal principal
    ) {
        User user = userLoginRepository.findByUsername(principal.getName()).get().getUser();
        Flight flight = flightRepository.findById(flightId).get();

        Seat targetSeat = seatRepository.findById(swapRequestRequest.getToSeatId()).get();
        Seat currentSeat = seatRepository.findById(swapRequestRequest.getFromSeatId()).get();
        SwapRequest swapRequest =
            new SwapRequest().builder().swapRequestStatus(SwapRequestStatus.PENDING)
                .flight(flight)
                .targetSeat(targetSeat)
                .currentSeat(currentSeat)
                .author(user)
                .build();

        swapRequestRepository.save(swapRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @GetMapping("/flight/{flightId}/swap-requests")
    public List<SwapRequestData> getSwapRequestsPerFlightAndUser(
        @PathVariable("flightId") Long flightId,
        @RequestParam("status") List<SwapRequestStatus> swapRequestStatuses,
        @Autowired Principal principal
    ) {
      User user = userLoginRepository.findByUsername(principal.getName()).get().getUser();
      Flight flight = flightRepository.findById(flightId).get();


      List<SwapRequestData> outgoingSW = swapRequestRepository.findByFlightAndAuthor(flight, user)
          .stream()
          .map(sw -> new SwapRequestData(sw, false)).collect(Collectors.toList());

      List<SwapRequestData> incomingSW = swapRequestRepository
          .findByFlightAndTargetSeats(flightId, outgoingSW
              .stream()
              .filter(sw -> sw.getSwapRequest().getSwapRequestStatus() == SwapRequestStatus.ACCEPTED).map(sw -> sw.getSwapRequest().getTargetSeat())

          )
              .stream()
              .map(sw -> new SwapRequestData(sw, true))
              .collect(Collectors.toList()));

      outgoingSW.addAll(incomingSW);
      return outgoingSW;
    }

    //да не даваме да резервират по 2 пъти една и съща седалка

}
