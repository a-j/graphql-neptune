package com.graphql.neptune;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;

public class Mutation implements GraphQLMutationResolver {
    private final SegmentRepository segmentRepository;

    public Mutation(SegmentRepository segmentRepository) {
        this.segmentRepository = segmentRepository;
    }

    public Segment createSegment(String id, String name) {
        Segment segment = new Segment(id, name, null, null);

        return segment;
    }
}
