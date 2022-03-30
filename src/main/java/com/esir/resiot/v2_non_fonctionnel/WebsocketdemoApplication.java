package com.esir.resiot.v2_non_fonctionnel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan("com.esir.resiot")
@EnableAutoConfiguration
public class WebsocketdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebsocketdemoApplication.class, args);
    }
}
