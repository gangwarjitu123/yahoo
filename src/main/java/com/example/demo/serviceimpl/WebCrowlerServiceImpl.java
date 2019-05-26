package com.example.demo.serviceimpl;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.demo.controller.UrlController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.service.WebCrowlerService;
import org.springframework.web.client.RestTemplate;

@Service
public class WebCrowlerServiceImpl implements WebCrowlerService {

	private static final String YAHOO = "yahoo";
	@Autowired
	private RestTemplate restTemplate;
	private static final String  URL = "https://in.yahoo.com";
	Set<String> set = new HashSet<String>();
	ExecutorService executorService= Executors.newFixedThreadPool(15);

	@Override
	public Set<String> getAllUrls(String url) throws Exception {
		String result=null;
		try {
		result = restTemplate.getForObject(url, String.class);
		}catch (Exception ex){
			return null;
		}

		if(result==null){
			return null;
		}
		String urlStrTemp = null;
         set.add("https://in.yahoo.com");
		 String regex = "href=\\\"(.*?)\\\"\"";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(result);

		if(!m.find()){
			return null;
		}
		while (m.find()) {
			String urlStr = m.group(1);
			urlStrTemp = urlStr.substring(0, urlStr.indexOf("\""));
			if (urlStrTemp.contains(YAHOO)) {
				if(set.add(urlStrTemp)) {
					 executorService.submit(new FindUrlInAsync(urlStrTemp));
				}
			}
		}
		executorService.awaitTermination(1,TimeUnit.MINUTES);
		return set;
	}

	class FindUrlInAsync implements  Runnable{
		private String url;
		FindUrlInAsync(String url){
			this.url=url;
		}
		@Override
		public void run() {
			try {
				    System.out.println(Thread.currentThread() + "url :"+ url);
					getAllUrls(url);
			}catch (Exception e){

			}
		}
	}


}
