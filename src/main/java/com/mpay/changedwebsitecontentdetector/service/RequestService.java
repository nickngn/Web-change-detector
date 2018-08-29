package com.mpay.changedwebsitecontentdetector.service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.mpay.changedwebsitecontentdetector.exception.URLException;
import com.mpay.changedwebsitecontentdetector.object.LinkObject;

@Service
public class RequestService {
	
	@Autowired
	private RestTemplate restTemplate;

	/**
	 * Send request to link
	 * @param link
	 * @return body of return; 
	 * @throws URLException
	 * @author HungND
	 */
	public String requestToLink(LinkObject link) throws URLException, RestClientException  {
		try {
			return callRequest(link.getLink());
		} catch (MalformedURLException | URISyntaxException e) {
			throw new URLException("URL khong dung dinh dang.");
		} catch (RestClientException e) {
			throw new RestClientException("Loi ket noi den url: " + link.getLink());
		} 
	}
	
	private String callRequest(String sUrl) 
			throws MalformedURLException, RestClientException, URISyntaxException {
		URL url = new URL(sUrl);
		ResponseEntity<String> response = restTemplate.getForEntity(url.toURI(), String.class);
		return response.getBody();
	}
}
