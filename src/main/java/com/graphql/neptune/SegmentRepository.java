package com.graphql.neptune;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;

import java.util.ArrayList;
import java.util.List;

public class SegmentRepository {
    private final GraphTraversalSource g;

    public SegmentRepository(GraphTraversalSource g) {
        this.g = g;
    }

    public List<Segment> getAllSegments() {
        List<Segment> segments = new ArrayList<>();

        GraphTraversal t = g.V().has(T.label, "SEGMENT").values("name");
        t.forEachRemaining(e ->
            segments.add(new Segment(e.toString(), null, null))
        );

        return segments;
    }

    public List<Segment> getSegments(String customerId) {
        List<Segment> segments = new ArrayList<>();

        GraphTraversal t = g.V().has(T.id, customerId).out("PART_OF");
        t.forEachRemaining(e ->
                segments.add(new Segment(e.toString(), null, null))
        );

        return segments;
    }
}
