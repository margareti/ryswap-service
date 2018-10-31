package com.example.demo.userflights;

import com.example.demo.flights.FoundFlight;

import java.util.List;
import java.util.Objects;

public class MyFlightResponse {

  private Long id;
  private FoundFlight flight;
  private List<SwapRequest> mySeats;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public FoundFlight getFlight() {
    return flight;
  }

  public void setFlight(FoundFlight flight) {
    this.flight = flight;
  }

  public List<SwapRequest> getMySeats() {
    return mySeats;
  }

  public void setMySeats(List<SwapRequest> mySeats) {
    this.mySeats = mySeats;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MyFlightResponse that = (MyFlightResponse) o;
    return Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

  @Override
  public String toString() {
    return "MyFlightResponse{" +
        "id=" + id +
        ", flight=" + flight +
        ", mySeats=" + mySeats +
        '}';
  }

  public MyFlightResponse(Long id, FoundFlight flight, List<SwapRequest> mySeats) {
    this.id = id;
    this.flight = flight;
    this.mySeats = mySeats;
  }
}
