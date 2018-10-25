package com.example.demo.userflights;

import com.example.demo.flights.seats.Seat;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class SwapRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserFlight userFlight;

    @ManyToOne
    @JoinColumn(name = "current_seat_id")
    private Seat currentSeat;

    @ManyToOne
    @JoinColumn(name = "target_seat_id")
    private Seat targetSeat;

    @Enumerated(EnumType.STRING)
    private SwapRequestStatus swapRequestStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserFlight getUserFlight() {
        return userFlight;
    }

    public void setUserFlight(UserFlight userFlight) {
        this.userFlight = userFlight;
    }

    public Seat getCurrentSeat() {
        return currentSeat;
    }

    public void setCurrentSeat(Seat currentSeat) {
        this.currentSeat = currentSeat;
    }

    public Seat getTargetSeat() {
        return targetSeat;
    }

    public void setTargetSeat(Seat targetSeat) {
        this.targetSeat = targetSeat;
    }

    public SwapRequestStatus getSwapRequestStatus() {
        return swapRequestStatus;
    }

    public void setSwapRequestStatus(SwapRequestStatus swapRequestStatus) {
        this.swapRequestStatus = swapRequestStatus;
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
                ", userFlight=" + userFlight +
                ", currentSeat=" + currentSeat +
                ", targetSeat=" + targetSeat +
                '}';
    }
}
