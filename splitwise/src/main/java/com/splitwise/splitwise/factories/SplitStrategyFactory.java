package com.splitwise.splitwise.factories;

import com.splitwise.splitwise.enums.SplitType;
import com.splitwise.splitwise.strategies.EqualSplitStrategy;
import com.splitwise.splitwise.strategies.PercentageSplitStrategy;
import com.splitwise.splitwise.strategies.SplitStrategy;
import com.splitwise.splitwise.strategies.UnequalSplitStrategy;

/**
 * Step 5a — FACTORY: maps SplitType enum → concrete SplitStrategy.
 *
 * ExpenseService calls this instead of switch/if-else on split type.
 * Adding a new split type = new Strategy class + one case here.
 */
public class SplitStrategyFactory {

    public SplitStrategy getStrategy(SplitType splitType) {
        return switch (splitType) {
            case EQUAL -> new EqualSplitStrategy();
            case UNEQUAL -> new UnequalSplitStrategy();
            case PERCENTAGE -> new PercentageSplitStrategy();
        };
    }
}
