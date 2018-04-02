package com.graphql.neptune;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;

import java.util.List;

public class Query implements GraphQLQueryResolver {
    private final CustomerRepository customerRepository;
    private final SegmentRepository segmentRepository;

    public Query(CustomerRepository customerRepository, SegmentRepository segmentRepository) {
        this.customerRepository = customerRepository;
        this.segmentRepository = segmentRepository;
    }

    public List<Segment> allSegments() {
        return segmentRepository.getAllSegments();
    }

    public List<Segment> segments(String customerId) {
        return segmentRepository.getSegmentsByCustomerId(customerId);
    }

    public Customer customer(String customerId) {
        Customer customer = customerRepository.getCustomer(customerId);
        customer.setSegments(segmentRepository.getSegmentsByCustomerId(customerId));
        return customer;
    }
}
