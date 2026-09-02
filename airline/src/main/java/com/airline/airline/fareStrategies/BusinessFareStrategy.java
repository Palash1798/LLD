package com.airline.airline.fareStrategies;

import com.airline.airline.enums.SeatClass;

import java.math.BigDecimal;

public class BusinessFareStrategy implements FareCalculationStrategy {

    @Override
    public BigDecimal calculate(BigDecimal baseFareEconomy) {
        return baseFareEconomy.multiply(new BigDecimal("2.5"));
    }

    @Override
    public SeatClass supports() {
        return SeatClass.BUSINESS;
    }
}
