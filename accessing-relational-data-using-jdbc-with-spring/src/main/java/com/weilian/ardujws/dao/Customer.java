package com.weilian.ardujws.dao;

import lombok.Data;

@Data
public class Customer {

    private Long id;

    private String firstName, lastName;

    public Customer(Long id, String firstName, String lastName){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
