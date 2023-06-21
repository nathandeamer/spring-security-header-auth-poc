package com.nathandeamer.security.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/auth")
    public String auth() {
        return "Authed!";
    }

    @GetMapping("/noauth")
    public String noAuth() {
        return "No Auth!";
    }

}
