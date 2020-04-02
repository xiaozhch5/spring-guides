package com.weilian.adwj.service;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;


// @Entity将其注册为数据库表
@Entity
@Data
public class Customer {

    // @Id表示数据库主键
    @Id
    private String id;

    private String firstName;

    private String lastName;

    public Customer(String id, String firstName, String lastName){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    protected Customer(){

    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
