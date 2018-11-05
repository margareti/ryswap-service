package com.example.demo.userflights;

import com.example.demo.flights.seats.Seat;
import com.example.demo.userflights.swaprequest.SwapRequest;

import java.util.List;

public class FlightSeat {

  private Seat seat;

  boolean isOccupied;
  boolean belongsToUser;

  public FlightSeat() {
  }

  public FlightSeat(
      Seat seat,
      Boolean isOccupied,
      Boolean belongsToUser) {
    this.seat = seat;
    this.isOccupied = isOccupied;
    this.belongsToUser = belongsToUser;
  }


  public FlightSeat seat(Seat seat) {
    this.setSeat(seat);
    return this;
  }

  public FlightSeat occupied(boolean occupied) {
    this.setOccupied(occupied);
    return this;
  }


  public FlightSeat doesBelongToUser(boolean belongsToUser) {
    this.setBelongsToUser(belongsToUser);
    return this;
  }


  public Seat getSeat() {
    return seat;
  }

  public void setSeat(Seat seat) {
    this.seat = seat;
  }

  public boolean isBelongsToUser() {
    return belongsToUser;
  }

  public void setBelongsToUser(boolean belongsToUser) {
    this.belongsToUser = belongsToUser;
  }

  public boolean isOccupied() {
    return isOccupied;
  }

  public void setOccupied(boolean occupied) {
    isOccupied = occupied;
  }
}
