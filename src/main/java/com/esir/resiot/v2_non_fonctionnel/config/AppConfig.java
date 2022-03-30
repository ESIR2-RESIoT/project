package com.esir.resiot.v2_non_fonctionnel.config;

import com.esir.resiot.v2_non_fonctionnel.servlet.RandomNameServlet;
import com.esir.resiot.v2_non_fonctionnel.util.DemoBeanUtil;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public ServletRegistrationBean socketServlet(){
        return new ServletRegistrationBean(new RandomNameServlet(), "/ws/random");
    }

    @Bean
    public DemoBeanUtil randomNameBeanUtil() {
        return new DemoBeanUtil();
    }
}