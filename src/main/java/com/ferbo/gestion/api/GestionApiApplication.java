package com.ferbo.gestion.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.ferbo.gestion.core.model", "com.ferbo.gestion.api.model"})
@ComponentScan(basePackages = {"com.ferbo.gestion"})
public class GestionApiApplication extends SpringBootServletInitializer
{

    public static void main(String[] args) {
        SpringApplication.run(GestionApiApplication.class, args);
    }
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(GestionApiApplication.class);
    }

}
