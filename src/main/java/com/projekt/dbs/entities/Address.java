package com.projekt.dbs.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Address {
    @Id
    @GeneratedValue
    @NotNull
    private Long addressId;

    @NotNull
    private String street;

    @NotNull
    private Integer houseNumber;

    @ManyToOne
    @JoinColumn(name = "zipCode")
    private ZIP zip;

    public Address(String street, Integer houseNumber, ZIP zip) {
        this.houseNumber = houseNumber;
        this.zip = zip;
        this.street = street;
    }

    public Address() {
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getStreet() {
        return street;
    }

    public Integer getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(Integer houseNumber) {
        this.houseNumber = houseNumber;
    }

    public ZIP getZip() {
        return zip;
    }

    public void setZip(ZIP zip) {
        this.zip = zip;
    }
}
