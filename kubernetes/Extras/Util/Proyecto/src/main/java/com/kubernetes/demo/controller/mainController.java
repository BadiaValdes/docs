package com.kubernetes.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api")
public class mainController {

    @Value("${kube.dato}")
    public String kanto;

    public mainController() {
    }

    @GetMapping()
    public String data(){
        return kanto;
    }
}
