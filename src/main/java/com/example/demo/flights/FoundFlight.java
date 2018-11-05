package com.example.demo.flights;

import com.example.demo.flights.airport.Airport;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

public class FoundFlight {

  private Long id;

  private Airport origin;
  private Airport destination;

  private LocalDateTime datetime;

  public FoundFlight(Long id, Airport origin, Airport destination, LocalDateTime datetime) {
    this.id = id;
    this.origin = origin;
    this.destination = destination;
    this.datetime = datetime;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Airport getOrigin() {
    return origin;
  }

  public void setOrigin(Airport origin) {
    this.origin = origin;
  }

  public Airport getDestination() {
    return destination;
  }

  public void setDestination(Airport destination) {
    this.destination = destination;
  }

  public LocalDateTime getDatetime() {
    return datetime;
  }

  public void setDatetime(LocalDateTime datetime) {
    this.datetime = datetime;
  }

}
