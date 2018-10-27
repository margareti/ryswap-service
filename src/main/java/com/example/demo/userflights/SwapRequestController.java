package com.example.demo.userflights;

import com.example.demo.flights.Flight;
import com.example.demo.flights.FlightRepository;
import com.example.demo.flights.seats.Seat;
import com.example.demo.flights.seats.SeatRepository;
import com.example.demo.flights.seats.SeatsConfiguration;
import com.example.demo.flights.seats.SeatsConfigurationRepository;
import com.example.demo.users.User;
import com.example.demo.users.login.UserLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<FlightSeat> getFlightSeats(@PathVariable("flightId") Long flightId) {
        SeatsConfiguration seatsConfiguration = seatsConfigurationRepository.findByFlightId(flightId).get();
        List<SwapRequest> swapRequests = swapRequestRepository.findByFlightId(flightId);

        return seatsConfiguration.getSeats().stream().map(s -> new FlightSeat(s,
                filterInboundSwapRequestsBySeatId(swapRequests, s),
                filterOutboundSwapRequestsBySeatId(swapRequests, s))).collect(Collectors.toList());
    }

    @PostMapping("/flight/{flightId}/my-seats")
    public List<SwapRequest> addMySeats(
        @PathVariable("flightId") Long flightId,
        @RequestBody List<Long> seatIds,
        @Autowired Principal principal) {
        // for each seat in seatIds, create Accepted SwapRequest
        User user = userLoginRepository.findByUsername(principal.getName()).get().getUser();
        Flight flight = flightRepository.findById(flightId).get();
        UserFlight userFlight = userFlightRepository.findByUserAndFlight(user, flight);
        return seatIds
            .stream()
            .map(si -> new SwapRequest(userFlight, null, seatRepository.findById(si).get(), SwapRequestStatus.ACCEPTED))
            .peek(swapRequest -> swapRequestRepository.save(swapRequest)).collect(Collectors.toList());

    }

    private List<SwapRequest> filterOutboundSwapRequestsBySeatId(List<SwapRequest> swapRequests, Seat seat) {
        return swapRequests.stream().filter(sr -> sr.getCurrentSeat().equals(seat)).collect(Collectors.toList());
    }

    private List<SwapRequest> filterInboundSwapRequestsBySeatId(List<SwapRequest> swapRequests, Seat seat) {
        return swapRequests.stream().filter(sr -> sr.getTargetSeat().equals(seat)).collect(Collectors.toList());
    }
}
