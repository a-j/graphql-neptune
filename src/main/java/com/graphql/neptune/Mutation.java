package com.graphql.neptune;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Mutation implements GraphQLMutationResolver {
    private static Logger logger = LoggerFactory.getLogger(Mutation.class);

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

//    public Customer refreshSegments(String customerId, List<String> segmentIds) {
//        segmentRepository.refreshSegmentsForCustomer(customerId, segmentIds);
//        Customer customer = customerRepository.getCustomer(customerId);
//        customer.setSegments(segmentRepository.getSegmentsByCustomerId(customerId));
//        return customer;
//    }

    public Customer refreshSegments(String customerId, List<String> segmentIds) {
        long startTime = System.nanoTime();
        List<String> customerIds = customerRepository.getCustomersSatisfyingCriteria();
        long endTime = System.nanoTime();
        logger.info("Got customers satisfying criteria in {}ms", (endTime - startTime)/1000000);

        startTime = System.nanoTime();
        segmentRepository.createSegmentsForCustomers(customerIds);
        endTime = System.nanoTime();
        logger.info("Created segment relations for customers in {}ms", (endTime - startTime)/1000000);

        Customer customer = new Customer("", "");
        return customer;
    }
}
