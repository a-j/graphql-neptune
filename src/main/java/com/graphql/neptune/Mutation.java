package com.graphql.neptune;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;

import java.util.List;

public class Mutation implements GraphQLMutationResolver {
    private final SegmentRepository segmentRepository;

    public Mutation(SegmentRepository segmentRepository) {
        this.segmentRepository = segmentRepository;
    }

    public Segment createSegment(String id, String name) {
        Segment segment = new Segment(id, name, null, null);
        this.segmentRepository.saveSegment(segment);
        return segment;
    }

    public Customer createCustomer(String id, String name, List<String> segmentIds) {
        Customer customer = new Customer(id, name);
        customer = this.segmentRepository.saveCustomer(customer, segmentIds);
        return customer;
    }
}
