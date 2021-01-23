package com.kr.media.amazing.controller;


import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("test")
public class HelloWordController {


    @GetMapping("hello")
    public String sayHello() {
        return "Hello World";
    }

}
