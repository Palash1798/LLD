package com.airline.airline.fareStrategies;

import com.airline.airline.enums.SeatClass;

import java.math.BigDecimal;

public interface FareCalculationStrategy {
    BigDecimal calculate(BigDecimal baseFareEconomy);
    SeatClass supports();
}
