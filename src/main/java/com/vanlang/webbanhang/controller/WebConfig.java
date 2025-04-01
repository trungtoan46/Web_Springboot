package com.vanlang.webbanhang.controller;

import com.vanlang.webbanhang.utils.PriceFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public PriceFormatter priceFormatter() {
        return new PriceFormatter();
    }
}
