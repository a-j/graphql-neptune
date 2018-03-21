package com.graphql.neptune;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SegmentRepository {
    private final GraphTraversalSource g;

    public SegmentRepository(GraphTraversalSource g) {
        this.g = g;
    }

    public List<Segment> getAllSegments() {
        List<Segment> segments = new ArrayList<>();

        List<Map<Object, Object>> valueMap = g.V().hasLabel("SEGMENT").valueMap(true).toList();

        for (Map map : valueMap) {
            segments.add(new Segment(map.get(T.id).toString(), map.get("name").toString(), null, null));
        }

        return segments;
    }

    public List<Segment> getSegments(String customerId) {
        List<Segment> segments = new ArrayList<>();

        List<Map<Object, Object>> valueMap = g.V().hasId(customerId).out("PART_OF").valueMap(true).toList();

        for (Map map : valueMap) {
            segments.add(new Segment(map.get(T.id).toString(), map.get("name").toString(), null, null));
        }

        return segments;
    }
}
