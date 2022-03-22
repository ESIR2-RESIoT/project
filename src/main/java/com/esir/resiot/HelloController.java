package com.esir.resiot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class HelloController {

    @GetMapping("/")
    public String index() throws IOException {
        StringBuffer stringBuffer = Utils.sendBuffer("index.html");
        return stringBuffer.toString();
    }

    @PostMapping("*")
    public void test(){
        System.out.println("test");
    }

}
