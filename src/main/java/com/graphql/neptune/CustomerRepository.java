package com.graphql.neptune;

import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomerRepository {
    private static Logger logger = LoggerFactory.getLogger(CustomerRepository.class);
    private final GraphTraversalSource g;

    public CustomerRepository(GraphTraversalSource g) {
        this.g = g;
    }

    public Customer getCustomer(String customerId) {
        Map<Object, Object> map = g.V().hasId(customerId).valueMap(true).next();

        Customer customer = new Customer(map.get(T.id).toString(), ((List) map.get("name")).get(0).toString());
        customer.setProfile(getProfile(customerId));

        return customer;
    }

    public Profile getProfile(String customerId) {
        Profile profile = new Profile();

        try {
            Map<Object, Object> map = g.V().hasId(customerId).out("HAS_PROFILE").valueMap(true).next();

            profile.setId(map.get(T.id).toString());
            profile.setAge(Integer.valueOf(((List) map.get("age")).get(0).toString()));
            profile.setAddress(((List) map.get("address")).get(0).toString());
            profile.setCity(((List) map.get("city")).get(0).toString());
            profile.setState(((List) map.get("state")).get(0).toString());
            profile.setZipcode(((List) map.get("zipcode")).get(0).toString());
        } catch (Exception e) {
            logger.error("Profile not found for user {}", customerId, e);
        }

        return profile;
    }

    public Customer saveCustomer(Customer customer, List<String> segmentIds) {
        try {
            g.addV("CUSTOMER").property(T.id, customer.getId()).property("name", customer.getName()).next();
            logger.info("Customer {} added", customer.getId());
        } catch (Exception e) {
            logger.error("Exception saving customer: ", e);
        }
        return customer;
    }

    public List<String> getCustomersSatisfyingCriteria() {
        List<String> customerIds = new ArrayList<>();
        try {
            Map<Object, Object> map = g.V().hasLabel("PROFILE").has("state", "Illinois").has("age", P.lte(60)).in("HAS_PROFILE")
                                        .valueMap(true).next();
            customerIds.add(map.get(T.id).toString());
        } catch (Exception e) {
            logger.error("Exception getting customers", e);
        }
        return customerIds;
    }
}
