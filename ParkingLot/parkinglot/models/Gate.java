package com.palash.parkinglot.models;

import com.palash.parkinglot.enums.GateStatus;
import com.palash.parkinglot.enums.GateType;

public class Gate extends BaseModel{
    private int gateNumber;
    private GateType gateType;
    private Operator currentOperator;
    private GateStatus gateStatus;

    public Gate(int gateNumber, GateType gateType, Operator currentOperator) {
        this.gateNumber = gateNumber;
        this.gateType = gateType;
        this.currentOperator = currentOperator;
        this.gateStatus = GateStatus.OPEN;
    }

    public int getGateNumber() {
        return gateNumber;
    }

    public void setGateNumber(int gateNumber) {
        this.gateNumber = gateNumber;
    }

    public GateType getGateType() {
        return gateType;
    }

    public void setGateType(GateType gateType) {
        this.gateType = gateType;
    }

    public Operator getCurrentOperator() {
        return currentOperator;
    }

    public void setCurrentOperator(Operator currentOperator) {
        this.currentOperator = currentOperator;
    }

    public GateStatus getGateStatus() {
        return gateStatus;
    }

    public void setGateStatus(GateStatus gateStatus) {
        this.gateStatus = gateStatus;
    }
}
