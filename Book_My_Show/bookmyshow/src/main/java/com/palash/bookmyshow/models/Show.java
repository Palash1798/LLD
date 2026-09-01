package com.palash.bookmyshow.models;

import com.palash.bookmyshow.enums.Feature;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "shows")
public class Show extends BaseModel{
    @ManyToOne
    private Screen screen;

    @ManyToOne
    private Movie movie;

    private Date StartTime;
    private Date endTime;

    @Enumerated(EnumType.ORDINAL)
    @ElementCollection
    private List<Feature> features;
}


// students and batches
// both are separate tables.

// batch
// batch_name, strength, dateOfCreation, student_ids

// student
// name, email, batch_id
// Saharsh, jkl, 1