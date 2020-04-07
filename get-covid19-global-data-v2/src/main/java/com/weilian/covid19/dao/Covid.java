package com.weilian.covid19.dao;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Covid {

    @Id
    private String objectId;

    private String longg;

    private String recovered;

    private String countryRegion;

    private String active;

    private String lastUpdate;

    private String deaths;

    private String confirmed;

    private String lat;

    public Covid() {
    }

    public Covid(String objectId, String longg, String recovered, String countryRegion, String active, String lastUpdate, String deaths, String confirmed, String lat){
        this.objectId = objectId;
        this.longg = longg;
        this.recovered = recovered;
        this.countryRegion = countryRegion;
        this.active = active;
        this.lastUpdate = lastUpdate;
        this.deaths = deaths;
        this.confirmed = confirmed;
        this.lat = lat;
    }

    @Override
    public String toString() {
        return objectId + "\t" + longg + "\t" + recovered + "\t" + countryRegion + "\t" + active + "\t" + lastUpdate + "\t" + deaths + "\t" + confirmed + "\t" + lat;
    }
}
