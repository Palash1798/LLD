package com.palash.bookmyshow.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass  // this class is a superclass of corrosponding tables.
public class BaseModel { //BaseModel will not going to be a table in database...therefore we dont have @Entity here.
    @Id   //representing primary key.
    //this Id should be Auto increment
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // auto increment.!
    private Long id;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)  //It is a time object
    private Date createdAt;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;
}



// BaseModel is a superclass.
// Booking, Payment, User, Show these are sub-classes
// sub-classes are the tables.

// MappedSuperclass -> My superclass attributes, will be
// the columns in sub-class tables