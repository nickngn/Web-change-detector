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
	 */
	public String requestToLink(LinkObject link) throws URLException {
		ResponseEntity<String> response;
		try {
			URL url = new URL(link.getLink());
			response = restTemplate.getForEntity(url.toURI(), String.class);
			return response.getBody();
		} catch (URISyntaxException | MalformedURLException e) {
			throw new URLException("Link khong hoat dong. " + link.toString(),e);
		} catch (Exception e){
			throw new RestClientException("Loi ket noi. " + link, e);
		}
	}
}
