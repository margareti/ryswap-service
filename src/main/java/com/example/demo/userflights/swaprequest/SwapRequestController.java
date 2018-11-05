package com.example.demo.userflights.swaprequest;

import com.example.demo.flights.Flight;
import com.example.demo.flights.FlightRepository;
import com.example.demo.flights.seats.Seat;
import com.example.demo.flights.seats.SeatRepository;
import com.example.demo.flights.seats.SeatsConfiguration;
import com.example.demo.flights.seats.SeatsConfigurationRepository;
import com.example.demo.userflights.FlightSeat;
import com.example.demo.userflights.UserFlight;
import com.example.demo.userflights.UserFlightRepository;
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
    public List<FlightSeat> getFlightSeats(@PathVariable("flightId") Long flightId,
                                           @Autowired Principal principal) {
        SeatsConfiguration seatsConfiguration = seatsConfigurationRepository.findByFlightId(flightId).get();
        List<SwapRequest> swapRequests = swapRequestRepository.findByFlightId(flightId);
        User user = userLoginRepository.findByUsername(principal.getName()).get().getUser();

        return seatsConfiguration.getSeats().stream().map(s ->  {

            List<SwapRequest> acceptedSWs = swapRequests.stream().filter(sw -> s.equals(sw.getTargetSeat()))
                .filter(sw -> sw.getSwapRequestStatus() == SwapRequestStatus.ACCEPTED).collect(Collectors.toList());
            return new FlightSeat()
                .seat(s)
                .occupied(!acceptedSWs.isEmpty())
                .doesBelongToUser(acceptedSWs.stream()
                    .filter(sw -> user.equals(sw.getUserFlight().getUser())).findFirst().isPresent());}

            ).collect(Collectors.toList());
    }

    @PostMapping("/flight/{flightId}/my-seats")
    public void  addMySeats(
        @PathVariable("flightId") Long flightId,
        @RequestBody List<Long> seatIds,
        @Autowired Principal principal) {
        // for each seat in seatIds, create Accepted SwapRequest
        User user = userLoginRepository.findByUsername(principal.getName()).get().getUser();
        Flight flight = flightRepository.findById(flightId).get();
        UserFlight userFlight = userFlightRepository.findByUserAndFlight(user, flight);
        userFlight.getSwapRequests().stream()
            .filter(sr -> !seatIds.stream()
                .filter(si -> sr.getSwapRequestStatus() == SwapRequestStatus.ACCEPTED &&
                    sr.getTargetSeat().getId().equals(si)).findFirst().isPresent())
            .peek(sr -> swapRequestRepository.delete(sr)).collect(Collectors.toList());

          seatIds
            .stream()
            .filter( sid -> ! userFlight.getSwapRequests().stream().filter(sr -> sr.getTargetSeat().getId().equals(sid)// :todo check accepted if needed
            ).filter(sr -> sr.getSwapRequestStatus() == SwapRequestStatus.ACCEPTED).findFirst().isPresent() )
            .map(si -> new SwapRequest(userFlight, null, seatRepository.findById(si).get(), SwapRequestStatus.ACCEPTED))
            .peek(swapRequest -> swapRequestRepository.save(swapRequest)).collect(Collectors.toList());



    }

    private List<SwapRequest> filterOutboundSwapRequestsBySeatId(List<SwapRequest> swapRequests, Seat seat) {
        return swapRequests.stream().filter(sr -> sr.getCurrentSeat() != null && sr.getCurrentSeat().equals(seat)).collect(Collectors.toList());
    }

    private List<SwapRequest> filterInboundSwapRequestsBySeatId(List<SwapRequest> swapRequests, Seat seat) {
        return swapRequests.stream().filter(sr -> sr.getTargetSeat() != null && sr.getTargetSeat().equals(seat)).collect(Collectors.toList());
    }
}
