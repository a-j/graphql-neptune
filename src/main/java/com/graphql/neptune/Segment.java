package com.graphql.neptune;

public class Segment {
    private final String id;
    private final String name;
    private final String validFrom;
    private final String validTo;

    public Segment(String name, String validFrom, String validTo) {
        this(null, name, validFrom, validTo);
    }

    public Segment(String id, String name, String validFrom, String validTo) {
        this.id = id;
        this.name = name;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public String getValidTo() {
        return validTo;
    }
}
