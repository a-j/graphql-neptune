package com.graphql.neptune;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SegmentRepository {
    private static Logger logger = LoggerFactory.getLogger(SegmentRepository.class);
    private final GraphTraversalSource g;

    public SegmentRepository(GraphTraversalSource g) {
        this.g = g;
    }

    public List<Segment> getAllSegments() {
        List<Segment> segments = new ArrayList<>();

        List<Map<Object, Object>> valueMap = g.V().hasLabel("SEGMENT").valueMap(true).toList();

        for (Map map : valueMap) {
            segments.add(new Segment(map.get(T.id).toString(), ((List) map.get("name")).get(0).toString(), null, null));
        }

        return segments;
    }

    public List<Segment> getSegments(String customerId) {
        List<Segment> segments = new ArrayList<>();

        List<Map<Object, Object>> valueMap = g.V().hasId(customerId).out("PART_OF").valueMap(true).toList();

        for (Map map : valueMap) {
            segments.add(new Segment(map.get(T.id).toString(), ((List) map.get("name")).get(0).toString(), null, null));
        }

        return segments;
    }

    public Profile getProfile(String customerId) {
        Map<Object, Object> map = g.V().hasId(customerId).out("HAS_PROFILE").valueMap(true).next();

        Profile profile = new Profile();
        profile.setId(map.get(T.id).toString());
        profile.setAge(Integer.valueOf(((List) map.get("age")).get(0).toString()));
        profile.setAddress(((List) map.get("address")).get(0).toString());
        profile.setCity(((List) map.get("city")).get(0).toString());
        profile.setState(((List) map.get("state")).get(0).toString());
        profile.setZipcode(((List) map.get("zipcode")).get(0).toString());

        return profile;
    }

    public Customer getCustomer(String customerId) {
        Map<Object, Object> map = g.V().hasId(customerId).valueMap(true).next();

        Customer customer = new Customer(map.get(T.id).toString(), ((List) map.get("name")).get(0).toString());
        customer.setProfile(getProfile(customerId));
        customer.setSegments(getSegments(customerId));

        return customer;
    }

    public void saveSegment(Segment segment) {
        try {
            g.addV("SEGMENT").property(T.id, segment.getId()).property("name", segment.getName()).next();
        } catch (Exception e) {
            logger.error("Exception saving segment: ", e);
        }
    }

    public Customer saveCustomer(Customer customer, List<String> segmentIds) {
        try {
            g.addV("CUSTOMER").property(T.id, customer.getId()).property("name", customer.getName()).next();
            logger.info("Customer {} added", customer.getId());

            if (segmentIds == null || segmentIds.size() <= 0) {
                return customer;
            }

            List<Segment> segments = new ArrayList<>();
            for (String segmentId : segmentIds) {
                g.V().hasId(segmentId).as("segmentVertex").V().hasId(customer.getId()).addE("PART_OF").to("segmentVertex");
                String segmentName = g.V().hasId(segmentId).values("name").next().toString();
                logger.info("Adding segment {}", segmentName);
                segments.add(new Segment(segmentId, segmentName, null, null));
            }
            logger.info("Segments for Customer {} saved", customer.getId());
            customer.setSegments(segments);
        } catch (Exception e) {
            logger.error("Exception saving customer: ", e);
        }
        return customer;
    }
}
