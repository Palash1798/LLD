package com.zoomcar.carrental.models;

import com.zoomcar.carrental.enums.PaymentMethod;
import com.zoomcar.carrental.enums.PaymentStatus;

import java.math.BigDecimal;

public class Payment extends BaseModel {
    private long reservationId;
    private BigDecimal amount;
    private PaymentMethod method;
    private PaymentStatus status;
    private String externalReference;

    public long getReservationId() {
        return reservationId;
    }

    public void setReservationId(long reservationId) {
        this.reservationId = reservationId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getExternalReference() {
        return externalReference;
    }

    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }
}
