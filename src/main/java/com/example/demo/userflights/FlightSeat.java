package com.example.demo.userflights;

import com.example.demo.flights.Flight;
import com.example.demo.flights.seats.Seat;
import com.example.demo.users.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;


@Entity
@Getter
@Setter
@Builder
public class FlightSeat {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private User owner;

  @ManyToOne
  private Flight flight;

  @ManyToOne
  private Seat seat;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof FlightSeat)) return false;
    FlightSeat that = (FlightSeat) o;
    return Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}


