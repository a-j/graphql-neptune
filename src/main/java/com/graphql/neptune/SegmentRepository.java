package com.graphql.neptune;

import java.util.ArrayList;
import java.util.List;

public class SegmentRepository {
    private List<Segment> segments;

    public SegmentRepository() {
        segments = new ArrayList<>();
        segments.add(new Segment("101", "Segment 1", "3/20/2018", "12/31/2018"));
        segments.add(new Segment("102", "Segment 2", "5/01/2018", "10/31/2018"));
        segments.add(new Segment("103", "Segment 3", "4/15/2018", "04/14/2019"));
    }

    public List<Segment> getAllSegments() {
        return segments;
    }
}
