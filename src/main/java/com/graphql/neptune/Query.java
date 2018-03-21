package com.graphql.neptune;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;

import java.util.List;

public class Query implements GraphQLQueryResolver {

    private final SegmentRepository segmentRepository;

    public Query(SegmentRepository segmentRepository) {
        this.segmentRepository = segmentRepository;
    }

    public List<Segment> allSegments() {
        return segmentRepository.getAllSegments();
    }

    public List<Segment> segments(String customerId) {
        return segmentRepository.getSegments(customerId);
    }

}
