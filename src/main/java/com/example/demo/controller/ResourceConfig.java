package com.example.demo.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ResourceConfig {

    @Bean
    public RestTemplate createRestTemplate(){
        RestTemplate restTemplate= new RestTemplate();
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory= new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setReadTimeout(1000);
        restTemplate.setRequestFactory(simpleClientHttpRequestFactory);
        return restTemplate;
    }
}
