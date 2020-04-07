package com.weilian.covid19.dao;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class TemplateDate {

    @Id
    private String tempDate;

    @Override
    public String toString() {
        return tempDate;
    }
}
