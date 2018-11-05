package com.example.demo.flights.seats;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;


@Entity
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer row;
    private String column;

    @JsonIgnore
    @ManyToOne
    private SeatsConfiguration seatsConfiguration;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public SeatsConfiguration getSeatsConfiguration() {
        return seatsConfiguration;
    }

    public void setSeatsConfiguration(SeatsConfiguration seatsConfiguration) {
        this.seatsConfiguration = seatsConfiguration;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return Objects.equals(id, seat.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Seat{" +
            "id=" + id +
            ", row=" + row +
            ", column='" + column + '\'' +
            '}';
    }
}
