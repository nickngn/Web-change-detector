package com.mpay.ccd.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mpay.ccd.CCD;
import com.mpay.ccd.dto.ConfigDto;
import com.mpay.ccd.exception.InvalidJsonException;
import com.mpay.ccd.exception.UnavailablePortException;
import com.mpay.ccd.model.ConfigModel;
import com.mpay.ccd.utils.IOUtils;

/**
 * The Class ConfigService.
 */
@Service
public class ConfigService {
	
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(ConfigService.class);
	
	/** The config. */
	private static ConfigModel config;
	
	/** The runningPort. */
	@Value("${server.port}")
	private int runningPort;
	
	/** The looping time. */
	@Value("${time}")
	private long currentLoopingTime;
	
	/**
	 * Gets the config.
	 *
	 * @return the config
	 */
	public static ConfigModel getConfig() {
		if (config == null) {
			try {
				reloadConfig();
			} catch (FileNotFoundException | InvalidJsonException e) {
				logger.error("Error loading config", e);
			}
		}
		
		return config;
	}
	
	/**
	 * Reload config.
	 *
	 * @throws FileNotFoundException the file not found exception
	 * @throws InvalidJsonException the invalid json exception
	 */
	public static void reloadConfig() throws FileNotFoundException, InvalidJsonException {
	  logger.info("Reload config");
		try (InputStream inputStream = new FileInputStream(IOUtils.getConfigFilePath())){
			config = new ObjectMapper().readValue(inputStream, ConfigModel.class);
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("Khong tim thay file 'config/config.json'");
		} catch (IOException e) {
			throw new InvalidJsonException("File config khong dung dinh dang");
		} finally {		  
		  logger.info("Config: {}", config);
		}
	}
	
	/**
	 * Update config.
	 *
	 * @param newConfig the config
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InvalidJsonException the invalid json exception
	 */
	public void updateConfig(ConfigModel newConfig) throws IOException, InvalidJsonException {
		logger.info("Update config: {}", newConfig);
		insertOldConfigs(newConfig);
		Path targetFilePath = new File(IOUtils.getConfigFilePath()).toPath();
		byte[] content = new ObjectMapper()
		    .enable(SerializationFeature.INDENT_OUTPUT)
		    .writeValueAsBytes(newConfig);
		Files.write(targetFilePath, content,StandardOpenOption.TRUNCATE_EXISTING);
		ConfigService.reloadConfig();
	}
	
	/**
	 * Insert old configs.
	 *
	 * @param config the config
	 */
	private void insertOldConfigs(ConfigModel config) {
	  ConfigModel oldConfig = ConfigService.getConfig();
    if (config.getSender() == null) {
      config.setSender(oldConfig.getSender());
    }
    
    if (config.getSenderPassword() == null) {
      config.setSenderPassword(oldConfig.getSenderPassword());
    }
    
    if (config.getReceivers() == null || config.getReceivers().isEmpty()) {
      config.setReceivers(oldConfig.getReceivers());
    }
    
    if (config.getLinks() == null || config.getLinks().isEmpty()) {
      config.setLinks(oldConfig.getLinks());
    }
	}
	
	/**
	 * Update config.
	 *
	 * @param newConfig the config
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InvalidJsonException the invalid json exception
	 * @throws UnavailablePortException the unavailable runningPort exception
	 */
	public void updateConfig(ConfigDto newConfig) throws IOException, InvalidJsonException, UnavailablePortException {
		updateConfig(newConfig.toConfigObject());
		boolean needRestart = false;
		if (newConfig.getPort() != runningPort) {
			needRestart = true;
			IOUtils.saveProperty("server.port", String.valueOf(newConfig.getPort()));
			if (!isTcpPortAvailable(newConfig.getPort())) {
				throw new UnavailablePortException("Port da bi su dung");
			}
		}
		
		if (newConfig.getLoopingTime() != currentLoopingTime) {
			needRestart = true;
			IOUtils.saveProperty("time", newConfig.getLoopingTime() + "");
		}
		
		if (needRestart) {
			CCD.restart();
		}
	}
	
	/**
	 * Checks if is tcp runningPort available.
	 *
	 * @param runningPort the runningPort
	 * @return true, if is tcp runningPort available
	 */
	public boolean isTcpPortAvailable(int port) {
	    try (ServerSocket serverSocket = new ServerSocket()) {
	        // setReuseAddress(false) is required only on OSX, 
	        // otherwise the code will not work correctly on that platform          
	        serverSocket.setReuseAddress(false);
	        serverSocket.bind(new InetSocketAddress(InetAddress.getByName("localhost"), port), 1);
	        return true;
	    } catch (Exception ex) {
	        return false;
	    }
	}  
}
