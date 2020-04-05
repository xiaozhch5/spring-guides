package com.weilian.jwt.authentication.controller;

import com.weilian.jwt.authentication.service.Greeting;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {

    private AtomicLong counter = new AtomicLong();

    private static final String template = "Hello world!";

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(name = "content", defaultValue = template) String content){
        return new Greeting(counter.incrementAndGet(), content);
    }

}
