package com.weilian.covid19.dao;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
@Data
public class ConfirmedDeathsRecovered {

    @Id
    private String confirmed;

    private String deaths;

    private String recovered;

    public ConfirmedDeathsRecovered() {
    }

    @Override
    public String toString() {
        return "ConfirmedDeathsRecovered{" +
                "confirmed='" + confirmed + '\'' +
                ", deaths='" + deaths + '\'' +
                ", recovered='" + recovered + '\'' +
                '}';
    }
}
