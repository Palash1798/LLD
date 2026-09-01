package com.palash.bookmyshow.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Seat extends BaseModel{
    private String seatNumber;   // number can be a column in mysql...therefor take seatNumber to avoid any confusion.

    @ManyToOne
    private SeatType seatType;

    private int rowValue;
    private  int columnValue;

}
