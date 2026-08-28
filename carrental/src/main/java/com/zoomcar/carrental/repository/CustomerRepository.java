package com.zoomcar.carrental.repository;

import com.zoomcar.carrental.models.Customer;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class CustomerRepository {
    private final Map<Long, Customer> customerTable = new TreeMap<>();
    private long previousId = 0L;

    public Customer saveCustomer(Customer customer) {
        previousId += 1;
        customer.setId(previousId);
        customerTable.put(previousId, customer);
        return customer;
    }

    public Optional<Customer> findCustomerById(long customerId) {
        return Optional.ofNullable(customerTable.get(customerId));
    }
}
