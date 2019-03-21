package com.example.demo.userflights;

import com.example.demo.flights.seats.Seat;
import com.example.demo.userflights.swaprequest.SwapRequest;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class FlightSeatData {

  private Seat seat;

  boolean isOccupied;
  boolean belongsToUser;



  public FlightSeatData() {
  }

  @Builder
  public FlightSeatData(
      Seat seat,
      Boolean isOccupied,
      Boolean belongsToUser) {
    this.seat = seat;
    this.isOccupied = isOccupied;
    this.belongsToUser = belongsToUser;
  }

}
