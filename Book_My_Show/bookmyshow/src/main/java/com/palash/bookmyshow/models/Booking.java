package com.palash.bookmyshow.models;

import com.palash.bookmyshow.enums.BookingStatus;
import com.palash.bookmyshow.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@Entity  //represent that, this class will be a table in DB.
         //@Entity works only for classes not for enums.

public class Booking extends BaseModel{
    //the object in the booking class, are going to be a relation int hte table.
    //when there is a relation, we have to define cardinalities.

    @ManyToOne
    private  User user;

    @ManyToOne
    private  Show show;

    private Date bookedAt;

    @ManyToMany
    private List<ShowSeat> showSeats;

    @Enumerated(EnumType.ORDINAL)   // Enumerated represents enums, and EnumType.ORDINAL => maintains the order.
    private BookingStatus bookingStatus;

    @OneToMany
    private List<Payment> payments;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    // give payment status message to user

    private  int amount;
}



// show s1 seat 50
// ShowSeat  s150
// booking b3 cancelled
// Booking b1 cancelled
// booking b2 confirmed

// booking table
// id, createdAt, user_id, show_id, amount, booking_status
// 1, now(), jayakrishan,   30,      300,    1
// 1, now(), shiva,         30,      300,    1
// 1, now(), rajat,         30,      300,    1

// the way you store the data, that is upto you!