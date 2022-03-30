package com.esir.resiot.v2_non_fonctionnel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/demo")
public class DemoController {

    @GetMapping("/randomNames")
    public String randomNames() {
        return "/randomNames.html";
    }
}
