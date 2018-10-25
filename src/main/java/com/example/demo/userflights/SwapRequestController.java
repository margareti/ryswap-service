package com.example.demo.userflights;

import com.example.demo.flights.seats.Seat;
import com.example.demo.flights.seats.SeatsConfiguration;
import com.example.demo.flights.seats.SeatsConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SwapRequestController {

    @Autowired
    private SeatsConfigurationRepository seatsConfigurationRepository;

    @Autowired
    private SwapRequestRepository swapRequestRepository;

    @GetMapping("/flight/{flightId}/seats")
    public List<FlightSeat> getFlightSeats(@PathVariable("flightId") Long flightId) {
        SeatsConfiguration seatsConfiguration = seatsConfigurationRepository.findByFlightId(flightId).get();
        List<SwapRequest> swapRequests = swapRequestRepository.findByFlightId(flightId);

        return seatsConfiguration.getSeats().stream().map(s -> new FlightSeat(s,
                filterInboundSwapRequestsBySeatId(swapRequests, s),
                filterOutboundSwapRequestsBySeatId(swapRequests, s))).collect(Collectors.toList());
    }

    private List<SwapRequest> filterOutboundSwapRequestsBySeatId(List<SwapRequest> swapRequests, Seat seat) {
        return swapRequests.stream().filter(sr -> sr.getCurrentSeat().equals(seat)).collect(Collectors.toList());
    }

    private List<SwapRequest> filterInboundSwapRequestsBySeatId(List<SwapRequest> swapRequests, Seat seat) {
        return swapRequests.stream().filter(sr -> sr.getTargetSeat().equals(seat)).collect(Collectors.toList());
    }
}
