package com.palash.bookmyshow.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseModel{
    private  String name;
    private String email;
    private String phoneNumber;

    @OneToMany
    private List<Booking> bookings;

    private  String password;
}
