package com.graphql.neptune;

import java.util.List;

public class Customer {
    private final String id;
    private final String name;
    private Profile profile;
    private List<Segment> segments;

    public Customer(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Profile getProfile() {
        return profile;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }
}
