package com.weilian.jwt.authentication.service;

import lombok.Data;

@Data
public class Greeting {

    private Long id;

    private String content;

    public Greeting(Long id, String content){
        this.id = id;
        this.content = content;
    }
}
