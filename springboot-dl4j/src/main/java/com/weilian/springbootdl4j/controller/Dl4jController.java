package com.weilian.springbootdl4j.controller;


import com.weilian.springbootdl4j.service.MLPClassifierMoon;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Dl4jController {

    @GetMapping("/")
    public String dl4jResult() throws Exception{
        return new MLPClassifierMoon().run();
    }

}
