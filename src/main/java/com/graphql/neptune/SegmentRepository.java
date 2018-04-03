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

    public List<Segment> getSegmentsByCustomerId(String customerId) {
        List<Segment> segments = new ArrayList<>();

        try {
            List<Map<Object, Object>> valueMap = g.V().hasId(customerId).out("PART_OF").valueMap(true).toList();

            for (Map map : valueMap) {
                segments.add(new Segment(map.get(T.id).toString(), ((List) map.get("name")).get(0).toString(), null, null));
            }
        } catch (Exception e) {
            logger.error("Segments not found for user {}", customerId, e);
        }

        return segments;
    }

    public List<Segment> getCurrentSegmentsByCustomerId(String customerId) {
        List<Segment> segments = new ArrayList<>();

        try {
            Map<Object, Object> map = g.V().hasLabel("DATA_LOAD").valueMap(true).next();
            Integer version = ((List<Integer>) map.get("version")).get(0);
            logger.info("Current data load version: {}", version);

            List<Map<Object, Object>> valueMap = g.V().hasId(customerId).outE("PART_OF")
                    .property("version", version).inV().valueMap(true).toList();

            for (Map entry : valueMap) {
                segments.add(new Segment(entry.get(T.id).toString(), ((List) entry.get("name")).get(0).toString(), null, null));
            }
        } catch (Exception e) {
            logger.error("Segments not found for user {}", customerId, e);
        }

        return segments;
    }

    public void saveSegment(Segment segment) {
        try {
            g.addV("SEGMENT").property(T.id, segment.getId()).property("name", segment.getName()).next();
        } catch (Exception e) {
            logger.error("Exception saving segment: ", e);
        }
    }

    public List<Segment> saveSegmentsForCustomer(Customer customer, List<String> segmentIds) {
        List<Segment> segments = new ArrayList<>();
        try {
            if (segmentIds == null || segmentIds.size() <= 0) {
                return segments;
            }

            for (String segmentId : segmentIds) {
                g.V().hasId(segmentId).as("segmentVertex").V().hasId(customer.getId()).addE("PART_OF").to("segmentVertex").next();
                String segmentName = g.V().hasId(segmentId).values("name").next().toString();
                logger.info("Adding segment {}", segmentName);
                segments.add(new Segment(segmentId, segmentName, null, null));
            }
            logger.info("Segments for Customer {} saved", customer.getId());
            customer.setSegments(segments);
        } catch (Exception e) {
            logger.error("Exception saving segments for customer: ", e);
        }
        return segments;
    }

    public void refreshSegmentsForCustomer(String customerId, List<String> segmentIds) {
        try {
            if (segmentIds == null || segmentIds.size() <= 0) {
                return;
            }
            logger.info("Customer Id: {} | Segment Ids: {}", customerId, segmentIds.toString());

            g.V().hasId(customerId).outE("PART_OF").drop();
            logger.info("Edges for customer id {} dropped", customerId);

            for (String segmentId : segmentIds) {
                logger.info("Associating segment {} to customer {}", segmentId, customerId);
                g.V().hasId(segmentId).as("segmentVertex").V().hasId(customerId).addE("PART_OF").to("segmentVertex");
            }
            logger.info("Segments for Customer {} refreshed", customerId);
        } catch (Exception e) {
            logger.error("Exception refreshing segments for customer: ", e);
        }
    }
}
