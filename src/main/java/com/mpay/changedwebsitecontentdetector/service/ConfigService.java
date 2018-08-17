package com.mpay.changedwebsitecontentdetector.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpay.changedwebsitecontentdetector.exception.ParseJsonException;
import com.mpay.changedwebsitecontentdetector.object.ConfigObject;

@Service
public class ConfigService {
	
	private static Logger logger = LoggerFactory.getLogger(ConfigService.class);
	
	private static ConfigObject config;
	
	public static ConfigObject getConfig() {
		if (config == null) {
			try {
				checkConfig();
			} catch (FileNotFoundException | ParseJsonException e) {
				logger.error("Error Loading config", e);
			}
		}
		
		return config;
	}
	
	public static void checkConfig() throws FileNotFoundException, ParseJsonException {
		FileInputStream fileInputStream = null;
			
		try {
			String path = new File(".").getCanonicalPath();
			String linkToFile = path + File.separator + "config" + File.separator + "config.json";
			fileInputStream = new FileInputStream(linkToFile);
			config = new ObjectMapper().readValue(fileInputStream, ConfigObject.class);
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("Khong tim thay file 'config/config.json'");
		} catch (IOException e) {
			throw new ParseJsonException("File config khong dung dinh dang");
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					// ignore this
				}
			}
		}
	}
	
}
