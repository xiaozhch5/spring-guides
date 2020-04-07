package com.weilian.covid19;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.weilian.covid19.dao.Covid;
import com.weilian.covid19.utils.JsoupHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Map;

@EnableScheduling
@SpringBootApplication
public class Covid19Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder){
        return applicationBuilder.sources(Covid19Application.class);
    }


    public static void main(String[] args) throws Exception {
        SpringApplication.run(Covid19Application.class, args);

    }
}


