package com.consume.consumeApi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/auth/demo")
public class demoController {

    @GetMapping
    public ResponseEntity<String> getHello(){
        return  ResponseEntity.ok("hello from secure place");
    }
}
