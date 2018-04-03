package com.graphql.neptune;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;

import java.util.List;

public class Mutation implements GraphQLMutationResolver {
    private final CustomerRepository customerRepository;
    private final SegmentRepository segmentRepository;

    public Mutation(CustomerRepository customerRepository, SegmentRepository segmentRepository) {
        this.customerRepository = customerRepository;
        this.segmentRepository = segmentRepository;
    }

    public Segment createSegment(String id, String name) {
        Segment segment = new Segment(id, name, null, null);
        segmentRepository.saveSegment(segment);
        return segment;
    }

    public Customer createCustomer(String id, String name, List<String> segmentIds) {
        Customer customer = new Customer(id, name);
        customer = customerRepository.saveCustomer(customer, segmentIds);
        customer.setSegments(segmentRepository.saveSegmentsForCustomer(customer, segmentIds));
        return customer;
    }

    public Customer refreshSegments(String customerId, List<String> segmentIds) {
        segmentRepository.refreshSegmentsForCustomer(customerId, segmentIds);
        Customer customer = customerRepository.getCustomer(customerId);
        customer.setSegments(segmentRepository.getSegmentsByCustomerId(customerId));
        return customer;
    }
}
