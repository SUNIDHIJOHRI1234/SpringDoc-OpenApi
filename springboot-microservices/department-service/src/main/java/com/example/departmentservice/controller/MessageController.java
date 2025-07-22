package com.example.departmentservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//changes in department properties (git file) and no need to restart the department service
//because we use @refreshscope so just hit the below Api's.
@RefreshScope// http://localhost:8080/actuator/refresh
@RestController
public class MessageController {
    @Value("${spring.boot.message}")
     private String message;

    // http://localhost:8080/message
    @GetMapping("message")
    public String message(){
        return message;

    }

}
