package com.example.demo.userflights.swaprequest;

import com.example.demo.flights.Flight;
import com.example.demo.flights.seats.Seat;
import com.example.demo.userflights.UserFlight;
import com.example.demo.users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
public class SwapRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    private Flight flight;

    @ManyToOne
    private User author;

    @ManyToOne
    @JoinColumn(name = "current_seat_id")
    private Seat currentSeat;

    @ManyToOne
    @JoinColumn(name = "target_seat_id")
    private Seat targetSeat;

    @Enumerated(EnumType.STRING)
    private SwapRequestStatus swapRequestStatus;


    public SwapRequest() {
    }

    @Builder
    public SwapRequest(Flight flight, Seat currentSeat, Seat targetSeat, SwapRequestStatus swapRequestStatus, User author) {
        this.flight = flight;
        this.currentSeat = currentSeat;
        this.targetSeat = targetSeat;
        this.swapRequestStatus = swapRequestStatus;
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SwapRequest that = (SwapRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SwapRequest{" +
                "id=" + id +
                ", flight=" + flight +
                ", currentSeat=" + currentSeat +
                ", targetSeat=" + targetSeat +
                '}';
    }
}
