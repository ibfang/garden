package com.example.garden;

import com.example.garden.runner.FirstRunner;
import com.example.garden.service.corporatefinance.C9Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GardenApplication {

    public static void main(String[] args) {
        SpringApplication.run(GardenApplication.class, args);
    }
//    @Bean
//    public FirstRunner firstRunner(){
//        return new FirstRunner();
//    }

}
