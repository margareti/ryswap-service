package com.example.demo.userflights;

import com.example.demo.userflights.swaprequest.SwapRequestStatus;

public class SwapRequestData {

  private  FlightSeat fromSeat;
  private  FlightSeat toSeat;
  //private boolean belongsToCurrentUser;
  private SwapRequestStatus status;

  public SwapRequestData(FlightSeat fromSeat, FlightSeat toSeat, SwapRequestStatus status) {
    this.fromSeat = fromSeat;
    this.toSeat = toSeat;
    this.status = status;
  }


  public FlightSeat getFromSeat() {
    return fromSeat;
  }

  public void setFromSeat(FlightSeat fromSeat) {
    this.fromSeat = fromSeat;
  }

  public FlightSeat getToSeat() {
    return toSeat;
  }

  public void setToSeat(FlightSeat toSeat) {
    this.toSeat = toSeat;
  }

  public SwapRequestStatus getStatus() {
    return status;
  }

  public void setStatus(SwapRequestStatus status) {
    this.status = status;
  }
}
