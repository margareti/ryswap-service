package com.example.demo.userflights;

import com.example.demo.flights.seats.Seat;

import java.util.List;

public class FlightSeat {
    private Seat seat;
    private List<SwapRequest> inboundSwapRequests;

    private List<SwapRequest> outboundSwapRequests;

    public FlightSeat(Seat seat, List<SwapRequest> inboundSwapRequests, List<SwapRequest> outboundSwapRequests) {
        this.seat = seat;
        this.inboundSwapRequests = inboundSwapRequests;
        this.outboundSwapRequests = outboundSwapRequests;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public List<SwapRequest> getInboundSwapRequests() {
        return inboundSwapRequests;
    }

    public void setInboundSwapRequests(List<SwapRequest> inboundSwapRequests) {
        this.inboundSwapRequests = inboundSwapRequests;
    }

    public List<SwapRequest> getOutboundSwapRequests() {
        return outboundSwapRequests;
    }

    public void setOutboundSwapRequests(List<SwapRequest> outboundSwapRequests) {
        this.outboundSwapRequests = outboundSwapRequests;
    }


}
