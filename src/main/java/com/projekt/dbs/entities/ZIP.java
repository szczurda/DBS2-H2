package com.projekt.dbs.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class ZIP {

    @Id
    @NotNull
    @GeneratedValue
    Integer zipCode;

    @NotNull
    String city;

    public ZIP() {

    }

    public ZIP(Integer zipCode, String city) {
        this.zipCode = zipCode;
        this.city = city;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
