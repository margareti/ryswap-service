package com.example.demo.flights;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "flight_route_time")
public class FlightRouteTime {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonIgnore
  @ManyToOne
  private FlightRoute flightRoute;

  private DayOfWeek dayOfWeek;
  private LocalTime time;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public FlightRoute getFlightRoute() {
    return flightRoute;
  }

  public void setFlightRoute(FlightRoute flightRoute) {
    this.flightRoute = flightRoute;
  }

  public DayOfWeek getDayOfWeek() {
    return dayOfWeek;
  }

  public void setDayOfWeek(DayOfWeek dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
  }

  public LocalTime getTime() {
    return time;
  }

  public void setTime(LocalTime time) {
    this.time = time;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FlightRouteTime that = (FlightRouteTime) o;
    return Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

  @Override
  public String toString() {
    return "FlightRouteTime{" +
        "id=" + id +
        ", flightRoute=" + flightRoute.getId() +
        ", weekday=" + dayOfWeek +
        ", time=" + time +
        '}';
  }

  public boolean matchesDayOfWeekAndTime(LocalDateTime datetime) {
    return datetime.getDayOfWeek().equals(this.getDayOfWeek()) &&
        datetime.getHour() == this.getTime().getHour() &&
        datetime.getMinute() == this.getTime().getMinute();
  }
}
