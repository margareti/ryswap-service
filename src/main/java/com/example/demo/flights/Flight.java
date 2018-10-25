package com.example.demo.flights;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

import com.example.demo.flights.route.FlightRouteTime;
import com.example.demo.flights.seats.SeatsConfiguration;

@Entity
public class Flight {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private FlightRouteTime flightRouteTime;

  private LocalDate flightDate;

  @ManyToOne
  private SeatsConfiguration seatsConfiguration;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public FlightRouteTime getFlightRouteTime() {
    return flightRouteTime;
  }

  public void setFlightRouteTime(FlightRouteTime flightRouteTime) {
    this.flightRouteTime = flightRouteTime;
  }

  public LocalDate getFlightDate() {
    return flightDate;
  }

  public void setFlightDate(LocalDate flightDate) {
    this.flightDate = flightDate;
  }

  public SeatsConfiguration getSeatsConfiguration() {
    return seatsConfiguration;
  }

  public void setSeatsConfiguration(SeatsConfiguration seatsConfiguration) {
    this.seatsConfiguration = seatsConfiguration;
  }

  @Override
  public String toString() {
    return "Flight{" +
        "id=" + id +
        ", flightRouteTime=" + flightRouteTime +
        ", flightDate=" + flightDate +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Flight flight = (Flight) o;
    return Objects.equals(getId(), flight.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}
