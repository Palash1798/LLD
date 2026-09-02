package com.airline.airline.services;

import com.airline.airline.factories.FareStrategyFactory;
import com.airline.airline.fareStrategies.FareCalculationStrategy;
import com.airline.airline.models.Flight;
import com.airline.airline.models.FlightSeat;

import java.math.BigDecimal;
import java.util.List;

public class FareCalculatorService {

    public BigDecimal calculateTotal(Flight flight, List<FlightSeat> seats) {
        BigDecimal total = BigDecimal.ZERO;
        for (FlightSeat seat : seats) {
            FareCalculationStrategy strategy = FareStrategyFactory.getStrategy(seat.getSeatClass());
            total = total.add(strategy.calculate(flight.getBaseFareEconomy()));
        }
        return total;
    }
}
