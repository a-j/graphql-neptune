package com.graphql.neptune;

public class Profile {
    private String id;
    private int age;
    private String address;
    private String city;
    private String state;
    private String zipcode;

    public Profile() {
    }

    public Profile(int age, String address, String city, String state, String zipcode) {
        this(null, age, address, city, state, zipcode);
    }

    public Profile(String id, int age, String address, String city, String state, String zipcode) {
        this.id = id;
        this.age = age;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public String getAddress() {
        return address;
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
