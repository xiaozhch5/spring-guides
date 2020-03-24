package com.weilian.carws.consumingrest;

import lombok.Data;

@Data
public class Value {

    private Long id;
    private String quote;
     public Value(Long id, String quote){
         this.id = id;
         this.quote = quote;
     }

    @Override
    public String toString() {
        return "Value{" +
                "id=" + id +
                ", quote='" + quote + '\'' +
                '}';
    }
}
