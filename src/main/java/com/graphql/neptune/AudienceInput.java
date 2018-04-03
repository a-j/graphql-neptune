package com.graphql.neptune;

public class AudienceInput {
    private String age;
    private String city;
    private String state;
    private String zipcode;

    public AudienceInput() {
    }

    public AudienceInput(String age, String city, String state, String zipcode) {
        this.age = age;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getAge() {
        return age;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipcode() {
        return zipcode;
    }
}
