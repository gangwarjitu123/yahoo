package com.example.demo.serviceimpl;


import java.util.HashMap;
import java.util.HashSet;

import java.util.Set;

import java.util.concurrent.ExecutorService;

import java.util.concurrent.Executors;

import java.util.concurrent.TimeUnit;

import java.util.regex.Matcher;

import java.util.regex.Pattern;


import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;


import com.example.demo.service.WebCrowlerService;


@Service

public class WebCrowlerServiceImpl implements WebCrowlerService {

    private static final String YAHOO = "yahoo";

    @Autowired

    private RestTemplate restTemplate;

    private static final String URL = "https://in.yahoo.com";


    ExecutorService executorService = Executors.newFixedThreadPool(25);


    @Override

    public  HashMap<String,String>  getAllUrls(String url) throws Exception {

        String result = null;
        HashMap<String,String> map= new HashMap<>();

        try {

            result = restTemplate.getForObject(url, String.class);

        } catch (Exception ex) {

            return null;

        }


        if (result == null) {

            return null;

        }

        String urlStrTemp = null;

        //   String regex = "href=\\\"(.*?)\\\"\"";

        String regex = "href=\\\"http(s):\\/\\/[A-Za-z0-9./-]+";

        Pattern p = Pattern.compile(regex);

        Matcher m = p.matcher(result);


        if (!m.find()) {

            return null;

        }

        while (m.find()) {

            String urlStr = m.group();

            urlStrTemp = urlStr.substring(6);

            if (urlStrTemp.contains(YAHOO)) {
                boolean flag = false;
                synchronized (Database.set) {
                    flag = Database.set.add(urlStrTemp);
                }
                if (flag) {

                    System.out.println(urlStrTemp);

                    executorService.submit(new FindUrlInAsync(urlStrTemp));

                }

            }

        }
        map.put("message","url fetching from yahoo in backend");
        return map;

    }


    class FindUrlInAsync implements Runnable {

        private String url;

        FindUrlInAsync(String url) {

            this.url = url;

        }

        @Override

        public void run() {

            try {

                System.out.println(Thread.currentThread() + "url :" + url);

                getAllUrls(url);

            } catch (Exception e) {


            }

        }

    }


}

