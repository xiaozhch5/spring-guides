package com.weilian.barwswsba.controller;


import com.weilian.barwswsba.service.Greeting;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.atomic.AtomicLong;

@Controller
public class GreetingController {

    private static final String template = "hello world!";

    private AtomicLong counter = new AtomicLong();

    @GetMapping("/hello-world")
    @ResponseBody
    public Greeting greeting(@RequestParam(name = "name", required = false, defaultValue = template) String name){
        return new Greeting(counter.incrementAndGet(), name);
    }

}
