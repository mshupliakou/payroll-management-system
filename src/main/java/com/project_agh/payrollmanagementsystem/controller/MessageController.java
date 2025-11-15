package com.project_agh.payrollmanagementsystem.controller;

import com.project_agh.payrollmanagementsystem.entities.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    @RequestMapping("/hello")
    public Message sayHello(){
        return new Message("Hello World") ;
    }
}
