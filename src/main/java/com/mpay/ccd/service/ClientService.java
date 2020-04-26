package com.mpay.ccd.service;

import com.mpay.ccd.exception.URLException;
import com.mpay.ccd.model.LinkModel;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * The Class ClientService.
 */
@Service
@AllArgsConstructor
public class ClientService {

	private final RestTemplate restTemplate;

	/**
	 * Send request to link.
	 *
	 * @author HungND
	 * @param link the link
	 * @return body of return;
	 * @throws URLException the URL exception
	 */
	public String callRequest(LinkModel link) throws URLException  {
		try {
			return callRequest(link.getLink());
		} catch (MalformedURLException | URISyntaxException e) {
			throw new URLException("URL khong dung dinh dang.");
		} 
	}
	
	/**
	 * Call request.
	 *
	 * @param sUrl the s url
	 * @return the string
	 * @throws MalformedURLException the malformed URL exception
	 * @throws URISyntaxException the URI syntax exception
	 */
	private String callRequest(String sUrl) 
			throws MalformedURLException, URISyntaxException {
		URL url = new URL(sUrl);
		ResponseEntity<String> response = restTemplate.getForEntity(url.toURI(), String.class);
		return response.getBody();
	}
}
