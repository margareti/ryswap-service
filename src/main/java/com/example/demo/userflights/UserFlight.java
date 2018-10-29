package com.example.demo.userflights;

import com.example.demo.flights.Flight;
import com.example.demo.users.User;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class UserFlight {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private User user;

  @ManyToOne
  private Flight flight;

  @OneToMany(mappedBy = "userFlight")
  private List<SwapRequest> swapRequests;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Flight getFlight() {
    return flight;
  }

  public void setFlight(Flight flight) {
    this.flight = flight;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserFlight that = (UserFlight) o;
    return Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

  @Override
  public String toString() {
    return "UserFlight{" +
        "id=" + id +
        ", user=" + user +
        ", flight=" + flight +
        '}';
  }

  public List<SwapRequest> getSwapRequests() {
    return swapRequests;
  }

  public void setSwapRequests(List<SwapRequest> swapRequests) {
    this.swapRequests = swapRequests;
  }
}
