package com.weilian.adwj.jparepository;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;


// @Entity将其注册为数据库表
@Entity
@Data
public class Customer {

    // @Id表示数据库主键
    @Id
    private Long id;

    private String firstName;

    private String lastName;

    public Customer(Long id, String firstName, String lastName){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Customer(){

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
