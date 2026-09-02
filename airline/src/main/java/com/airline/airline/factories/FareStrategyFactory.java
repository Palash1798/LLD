package com.airline.airline.factories;

import com.airline.airline.enums.SeatClass;
import com.airline.airline.fareStrategies.BusinessFareStrategy;
import com.airline.airline.fareStrategies.EconomyFareStrategy;
import com.airline.airline.fareStrategies.FareCalculationStrategy;
import com.airline.airline.fareStrategies.FirstClassFareStrategy;

public class FareStrategyFactory {

    public static FareCalculationStrategy getStrategy(SeatClass seatClass) {
        if (seatClass == SeatClass.BUSINESS) {
            return new BusinessFareStrategy();
        } else if (seatClass == SeatClass.FIRST) {
            return new FirstClassFareStrategy();
        } else {
            return new EconomyFareStrategy();
        }
    }
}
