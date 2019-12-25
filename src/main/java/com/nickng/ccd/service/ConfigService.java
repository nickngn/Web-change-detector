package com.nickng.ccd.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.nickng.ccd.CCD;
import com.nickng.ccd.dto.ConfigDto;
import com.nickng.ccd.exception.InvalidJsonException;
import com.nickng.ccd.exception.UnavailablePortException;
import com.nickng.ccd.model.ConfigModel;
import com.nickng.ccd.utils.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * The Class ConfigService.
 */
@Service
@Slf4j
public class ConfigService {

    /**
     * The config.
     */
    private static ConfigModel config;

    /**
     * The runningPort.
     */
    @Value("${server.port}")
    private int runningPort;

    /**
     * The looping time.
     */
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
                log.error("Error loading config", e);
            }
        }

        return config;
    }

    /**
     * Reload config.
     *
     * @throws FileNotFoundException the file not found exception
     * @throws InvalidJsonException  the invalid json exception
     */
    public static void reloadConfig() throws FileNotFoundException, InvalidJsonException {
        log.info("Reload config");
        try (InputStream inputStream = new FileInputStream(IOUtils.getConfigFilePath())) {
            config = new ObjectMapper().readValue(inputStream, ConfigModel.class);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Khong tim thay file 'config/config.json'");
        } catch (IOException e) {
            throw new InvalidJsonException("File config khong dung dinh dang");
        } finally {
            log.info("Config: {}", config);
        }
    }

    /**
     * Update config.
     *
     * @param config the config
     * @throws IOException              Signals that an I/O exception has occurred.
     * @throws InvalidJsonException     the invalid json exception
     * @throws UnavailablePortException the unavailable runningPort exception
     */
    public void updateConfig(ConfigDto config) throws IOException, InvalidJsonException, UnavailablePortException {
        updateConfig(config.toConfigObject());
        boolean needRestart = false;
        if (config.getPort() != runningPort) {
            needRestart = true;
            IOUtils.saveProperty("server.port", String.valueOf(config.getPort()));
            if (!isAvailable(config.getPort())) {
                throw new UnavailablePortException("Port da bi su dung");
            }
        }

        if (config.getLoopingTime() != currentLoopingTime) {
            needRestart = true;
            IOUtils.saveProperty("time", config.getLoopingTime() + "");
        }

        if (needRestart) {
            CCD.restart();
        }
    }

    /**
     * Update config.
     *
     * @param newConfig the config
     * @throws IOException          Signals that an I/O exception has occurred.
     * @throws InvalidJsonException the invalid json exception
     */
    public void updateConfig(ConfigModel newConfig) throws IOException, InvalidJsonException {
        log.info("Update config: {}", newConfig);
        insertOldConfigs(newConfig);
        Path targetFilePath = new File(IOUtils.getConfigFilePath()).toPath();
        byte[] content = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .writeValueAsBytes(newConfig);
        Files.write(targetFilePath, content, StandardOpenOption.TRUNCATE_EXISTING);
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
     * Checks if port is available.
     *
     * @param port the running port
     * @return if port is available
     */
    private boolean isAvailable(int port) {
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
