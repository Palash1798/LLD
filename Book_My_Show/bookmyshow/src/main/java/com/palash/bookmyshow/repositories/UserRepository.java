package com.palash.bookmyshow.repositories;

import com.palash.bookmyshow.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // we dont need to write a DB query to get the object by id from table
    // we have to use JpaRepository to get the data automatically.

//    public Optional<User> getUSerById(Long userId){
//        // response = db.execute(select * from user where user_id = userId);
//        // Optional.of(response)
//        return Optional.empty();
//    }

    public Optional<User> findByEmail(String emailId);

}
