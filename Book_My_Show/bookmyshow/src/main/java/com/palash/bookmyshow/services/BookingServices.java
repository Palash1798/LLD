package com.palash.bookmyshow.services;

import com.palash.bookmyshow.enums.BookingStatus;
import com.palash.bookmyshow.enums.ShowSeatStatus;
import com.palash.bookmyshow.exceptions.ShowNotFoundException;
import com.palash.bookmyshow.exceptions.ShowSeatNotAvailableException;
import com.palash.bookmyshow.exceptions.UserNotFoundException;
import com.palash.bookmyshow.models.Booking;
import com.palash.bookmyshow.models.Show;
import com.palash.bookmyshow.models.ShowSeat;
import com.palash.bookmyshow.models.User;
import com.palash.bookmyshow.repositories.BookingRepository;
import com.palash.bookmyshow.repositories.ShowRepository;
import com.palash.bookmyshow.repositories.ShowSeatRepository;
import com.palash.bookmyshow.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.*;

@Service
public class BookingServices {
    private UserRepository userRepository;
    private ShowRepository showRepository;
    private ShowSeatRepository showSeatRepository;
    private BookingRepository bookingRepository;
    private PriceCalculatorService priceCalculatorService;

    @Autowired
    public BookingServices(UserRepository userRepository,
                           ShowRepository showRepository,
                           ShowSeatRepository showSeatRepository,
                           BookingRepository bookingRepository,
                           PriceCalculatorService priceCalculatorService){
        this.userRepository = userRepository;
        this.showRepository = showRepository;
        this.showSeatRepository = showSeatRepository;
        this.bookingRepository = bookingRepository;
        this.priceCalculatorService = priceCalculatorService;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Booking bookMovie(Long userId, Long showId, List<Long> showSeatId) throws UserNotFoundException, ShowNotFoundException, ShowSeatNotAvailableException {
        // -------- start transaction (take a soft lock) ----------

        // 1. get the user from the userID
        // 2. get the show from the showId

        // ideally transaction should start from point 3 to point 7.

        // 3. get the ShowSeat object, from the showSeatIds
        // 4. check if the ShowSeats are available = AVAILABLE, or BLOCKED after 15 mins.
        // 5. if any ShowSeat is not available, throw an error
        // 6. update the status to BLOCKED. update the lockedAt time.
        // 7. save the ShowSeats to the DB.

        // 8. create the Booking object.
        // 9. return the Booking object to the controller.

        // --------- end transaction (release the lock) -------


        // 1. get the user
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()){
            throw new UserNotFoundException("User not found in database");
        }
        User bookedBy = optionalUser.get();

        //2. get the show
        Optional<Show> optionalShow = showRepository.findById(showId);
        if(optionalShow.isEmpty()){
            throw new ShowNotFoundException("Show not found");
        }
        Show bookedShow = optionalShow.get();

        //3. get the showSeat object.
        List<ShowSeat> showSeats = showSeatRepository.findAllById(showSeatId);

        // 4. check if the ShowSeats are available = AVAILABLE, or BLOCKED after 15 mins.
        for(ShowSeat showSeat : showSeats){
            Date lockedAt = showSeat.getLockedAt();
            ShowSeatStatus status = showSeat.getStatus();
            if(status == ShowSeatStatus.BOOKED ||
                    (status == ShowSeatStatus.BLOCKED && Duration.between(new Date().toInstant(), lockedAt.toInstant()).toMinutes() < 15)){
                throw new ShowSeatNotAvailableException("seat not available");
            }
        }

        //5. Update the status of Available Seats.
        for(ShowSeat showSeat : showSeats){
            showSeat.setStatus(ShowSeatStatus.BLOCKED);
            showSeat.setLockedAt(new Date());
        }

        //6. save to Database
        List<ShowSeat> savedShowSeats = showSeatRepository.saveAll(showSeats);

        //
        Booking booking = new Booking();
        booking.setUser(bookedBy);
        booking.setShow(bookedShow);
        booking.setBookedAt(new Date());
        booking.setShowSeats(savedShowSeats);
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setAmount(priceCalculatorService.calculatePrice(savedShowSeats,bookedShow));
        booking.setPayments(new ArrayList<>());

        //
        Booking savedBooking = bookingRepository.save(booking);

        return savedBooking;
    }
}
