package com.example.demo.userflights.swaprequest;

import com.example.demo.flights.Flight;
import com.example.demo.flights.FlightRepository;
import com.example.demo.flights.seats.Seat;
import com.example.demo.flights.seats.SeatRepository;
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


    @Autowired
    private FlightSeatRepository flightSeatRepository;


    @PostMapping("/flight/{flightId}/swap-request")
    public ResponseEntity makeSwapRequest(
        @PathVariable("flightId") Long flightId,
        @RequestBody SwapRequestRequest swapRequestRequest,
        @Autowired Principal principal) {
        User user = userLoginRepository.findByUsername(principal.getName()).get().getUser();
        Flight flight = flightRepository.findById(flightId).get();

        Seat targetSeat = seatRepository.findById(swapRequestRequest.getToSeatId()).get();
        Seat currentSeat = seatRepository.findById(swapRequestRequest.getFromSeatId()).get();

        if (!swapRequestRepository
                .findByFlightAndAuthorAndSwapRequestStatusAndCurrentSeatAndTargetSeat(
                    flight,
                    user,
                    SwapRequestStatus.PENDING,
                    currentSeat,
                    targetSeat).isEmpty()
        ) {
            throw new RuntimeException("This swap request already exists");
        }
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
        @Autowired Principal principal) {
      User user = userLoginRepository.findByUsername(principal.getName()).get().getUser();
      Flight flight = flightRepository.findById(flightId).get();
      List<FlightSeat> userSeats = flightSeatRepository.findByOwnerAndFlight(user, flight);

      List<SwapRequestData> outgoingSW = swapRequestRepository.findByFlightAndAuthor(flight, user)
          .stream()
          .map(sw -> new SwapRequestData(sw, false))
          .collect(Collectors.toList());

      List<SwapRequestData> incomingSW =
          swapRequestRepository
            .findByFlightAndTargetSeats(flightId, userSeats.stream().map(seat -> seat.getSeat()).collect(Collectors.toList()))
              .stream()
              .map(sw -> new SwapRequestData(sw, true))
              .collect(Collectors.toList());

      outgoingSW.addAll(incomingSW);

      return outgoingSW
          .stream()
          .filter(sw -> swapRequestStatuses.contains(sw.getSwapRequest().getSwapRequestStatus()))
          .collect(Collectors.toList());
    }

    @PostMapping("swap-request/{swapRequestId}/decline")
    public void declineSwapRequest(
        @PathVariable("swapRequestId") Long swapRequestId,
        @Autowired Principal principal) {
        User user = userLoginRepository.findByUsername(principal.getName()).get().getUser();
        SwapRequest srToDecline = swapRequestRepository.getOne(swapRequestId);

        if (srToDecline == null) {
            throw new RuntimeException("Whacha doin there, son? We have no records of such a swap request!");
        }
        SwapRequest ownerSR = swapRequestRepository
            .findByFlightAndAuthorAndSwapRequestStatus(srToDecline.getFlight(), user, SwapRequestStatus.ACCEPTED)
            .stream()
            .filter(sr -> sr.getTargetSeat().equals(srToDecline.getTargetSeat()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("You don't own this seat, my dear"));
        srToDecline.setSwapRequestStatus(SwapRequestStatus.DECLINED);
        swapRequestRepository.save(srToDecline);
    }

    @PostMapping("swap-request/{swapRequestId}/cancel")
    public void cancelSwapRequest (
        @PathVariable("swapRequestId") Long swapRequestId,
        @Autowired Principal principal) {
        User user = userLoginRepository.findByUsername(principal.getName()).get().getUser();
        SwapRequest srToCancel = swapRequestRepository.getOne(swapRequestId);

        if (srToCancel == null) {
            throw new RuntimeException("Whacha doin there, son? We have no records of such a swap request!");
        }

        if (srToCancel.getAuthor().equals(user) && srToCancel.getSwapRequestStatus().equals(SwapRequestStatus.PENDING)) {
            srToCancel.setSwapRequestStatus(SwapRequestStatus.CANCELLED);
            swapRequestRepository.save(srToCancel);
        } else {
            throw new RuntimeException("Can't cancel, because the swap request isn't yours or it is too late to cancel");
        }
    }

    @PostMapping("swap-request/{swapRequestId}/accept")
    public void acceptSwapRequest(
        @PathVariable("swapRequestId") Long swapRequestId,
        @Autowired Principal principal) {
        User user = userLoginRepository.findByUsername(principal.getName()).get().getUser();
        SwapRequest srToAccept = swapRequestRepository.getOne(swapRequestId);
        FlightSeat swTarget = flightSeatRepository.findByFlightAndSeat(srToAccept.getFlight(), srToAccept.getTargetSeat());

        if (srToAccept == null) {
            throw new RuntimeException("Whacha doin there, son? We have no records of such a swap request!");
        }

        if (swTarget == null || !user.equals(swTarget.getOwner())) {
            new RuntimeException("You don't own this seat, my dear");
        }

        srToAccept.setSwapRequestStatus(SwapRequestStatus.ACCEPTED);
        swapRequestRepository.save(srToAccept);

        FlightSeat authorsSeat = flightSeatRepository.findByFlightAndSeat(srToAccept.getFlight(), srToAccept.getCurrentSeat());
        authorsSeat.setOwner(user);
        swTarget.setOwner(srToAccept.getAuthor());
        flightSeatRepository.save(authorsSeat);
        flightSeatRepository.save(swTarget);

    }

}
