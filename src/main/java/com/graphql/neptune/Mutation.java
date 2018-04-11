package com.graphql.neptune;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
        logger.info("Number of customers satisfying criteria: {}", customerIds.size());
        logger.info("Got customers satisfying criteria in {}ms", (endTime - startTime)/1000000);

        startTime = System.nanoTime();
        List<List<String>> customersList = Lists.partition(customerIds, 20);
        try {
            ExecutorService executor = Executors.newFixedThreadPool(20);
            for (List<String> subList : customersList) {
                executor.execute(new SegmentsCreator(segmentRepository, customerIds));
            }
            executor.shutdown();
            executor.awaitTermination(2, TimeUnit.HOURS);

        } catch (Exception e) {
            logger.error("Exception occurred", e);
        }
        endTime = System.nanoTime();
        logger.info("Created segment relations for customers in {}ms", (endTime - startTime)/1000000);

        Customer customer = new Customer("", "");
        return customer;
    }
}
class SegmentsCreator implements Runnable {
    private final SegmentRepository segmentRepository;
    private final List<String> customerIds;

    public SegmentsCreator (SegmentRepository segmentRepository, List<String> customerIds) {
        this.segmentRepository = segmentRepository;
        this.customerIds = customerIds;
    }

    @Override
    public void run() {
        try {
            segmentRepository.createSegmentsForCustomers(customerIds);
        } catch (Exception e) {
            System.err.println("Exception occurred in thread");
            e.printStackTrace();
        }
    }
}
