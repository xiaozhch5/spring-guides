package com.weilian.barwswsba.service;

import lombok.Data;

@Data
public class Greeting {

    private long id;

    private String content;

    public Greeting(long id, String content){
        this.content = content;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Greeting{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }
}
