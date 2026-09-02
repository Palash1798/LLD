package com.airline.airline.fareStrategies;

import com.airline.airline.enums.SeatClass;

import java.math.BigDecimal;

public class EconomyFareStrategy implements FareCalculationStrategy {

    @Override
    public BigDecimal calculate(BigDecimal baseFareEconomy) {
        return baseFareEconomy;
    }

    @Override
    public SeatClass supports() {
        return SeatClass.ECONOMY;
    }
}
