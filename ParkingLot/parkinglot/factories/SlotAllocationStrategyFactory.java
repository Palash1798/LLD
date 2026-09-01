package com.palash.parkinglot.factories;

import com.palash.parkinglot.enums.SlotAllocationStrategyType;
import com.palash.parkinglot.strategies.slotAllocationStrategy.FarthestSlotAllocationStrategy;
import com.palash.parkinglot.strategies.slotAllocationStrategy.NearestSlotAllocationStrategy;
import com.palash.parkinglot.strategies.slotAllocationStrategy.RandomSlotAllocationStrategy;
import com.palash.parkinglot.strategies.slotAllocationStrategy.SlotAllocationStrategy;

public class SlotAllocationStrategyFactory {
    public static SlotAllocationStrategy getSlotAllocationStrategyByStrategyType(SlotAllocationStrategyType type){
        if(type == SlotAllocationStrategyType.RANDOM){
            return new RandomSlotAllocationStrategy();
        }
        else if(type == SlotAllocationStrategyType.NEAREST_TO_GATE){
            return new NearestSlotAllocationStrategy();
        }
        else{
            return new FarthestSlotAllocationStrategy();
        }
    }
}
