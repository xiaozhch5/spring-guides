package com.weilian.covid19.dao;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Covid {

    @Id
    private String country;

    private String confirmed;

    private String deaths;

    private String caseFatality;

    private String deaths100k;

    public Covid() {
    }

    public Covid(String country, String confirmed, String deaths, String caseFatality, String deaths100k){
        this.country = country;
        this.confirmed = confirmed;
        this.deaths = deaths;
        this.caseFatality = caseFatality;
        this.deaths100k = deaths100k;
    }

    @Override
    public String toString() {
        return country + "\t" + confirmed + "\t" + deaths + "\t" + caseFatality + "\t" + deaths100k;
    }
}
