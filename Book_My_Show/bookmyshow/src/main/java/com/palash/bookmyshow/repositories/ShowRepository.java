package com.palash.bookmyshow.repositories;

import com.palash.bookmyshow.models.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
//    default public Optional<Show> getShowByScreen(Long screenId){
//        return null;
//    }
}
