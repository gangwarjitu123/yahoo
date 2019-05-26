package com.example.demo.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.demo.serviceimpl.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.service.WebCrowlerService;

@RestController
public class UrlController {

@Autowired
private RestTemplate restTemplate;

@Autowired
private WebCrowlerService webCrowlerService;

private static final String  URL = "https://in.yahoo.com";

	
    @GetMapping("/getUrlData")
	public ResponseEntity<Object> getdata() throws Exception{
	   HashMap<String,String> map= webCrowlerService.getAllUrls(URL);
	   return new ResponseEntity<>(map, HttpStatus.OK);

    }


	@GetMapping("/urls")
	public ResponseEntity<Object> urls() throws Exception{
		return new ResponseEntity<>(Database.set, HttpStatus.OK);

	}

}
