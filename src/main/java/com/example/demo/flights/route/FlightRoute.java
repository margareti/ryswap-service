package com.example.demo.flights.route;


import javax.persistence.*;
import java.util.List;
import java.util.Objects;

import com.example.demo.flights.airport.Airport;

@Entity
public class FlightRoute {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private Airport origin;

  @ManyToOne
  private Airport destination;

  @OneToMany(mappedBy = "flightRoute", cascade = CascadeType.ALL)
  private List<FlightRouteTime> flightRouteTimes;

  public List<FlightRouteTime> getFlightRouteTimes() {
    return flightRouteTimes;
  }

  public void setFlightRouteTimes(List<FlightRouteTime> flightRouteTimes) {
    this.flightRouteTimes = flightRouteTimes;
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


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FlightRoute that = (FlightRoute) o;
    return Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

  @Override
  public String toString() {
    return "FlightRoute{" +
        "id=" + id +
        ", origin=" + origin +
        ", destination=" + destination +
        '}';
  }
}
