package com.paymentgateway.paymentgateway.dto;

import com.paymentgateway.paymentgateway.enums.PaymentMethod;
import com.paymentgateway.paymentgateway.models.PaymentInstrumentDetails;

/**
 * Step 3 — API input when customer pays for an existing order.
 */
public class ProcessPaymentRequest {

    private final String orderId;
    private final PaymentMethod method;
    private final PaymentInstrumentDetails instrumentDetails;

    public ProcessPaymentRequest(String orderId,
                                   PaymentMethod method,
                                   PaymentInstrumentDetails instrumentDetails) {
        this.orderId = orderId;
        this.method = method;
        this.instrumentDetails = instrumentDetails;
    }

    public String getOrderId() {
        return orderId;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public PaymentInstrumentDetails getInstrumentDetails() {
        return instrumentDetails;
    }
}
