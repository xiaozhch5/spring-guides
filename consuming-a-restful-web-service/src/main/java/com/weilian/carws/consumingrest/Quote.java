package com.weilian.carws.consumingrest;

import lombok.Data;

@Data
public class Quote {

    private String type;
    private Value value;

    public Quote(String type, Value value){
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "type='" + type + '\'' +
                ", value=" + value +
                '}';
    }
}
