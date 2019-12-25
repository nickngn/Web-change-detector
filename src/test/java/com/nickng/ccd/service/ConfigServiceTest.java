package com.nickng.ccd.service;

import com.nickng.ccd.dto.ConfigDto;
import com.nickng.ccd.exception.InvalidJsonException;
import com.nickng.ccd.exception.UnavailablePortException;
import com.nickng.ccd.model.ConfigModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigServiceTest {

    private ConfigService configServiceUnderTest;

    @BeforeEach
    void setUp() {
        configServiceUnderTest = new ConfigService();
    }

    @Test
    void testUpdateConfig() throws Exception {
        // Setup
        final ConfigModel newConfig = new ConfigModel();

        // Run the test
        configServiceUnderTest.updateConfig(newConfig);

        // Verify the results
    }

    @Test
    void testUpdateConfig_ThrowsIOException() {
        // Setup
        final ConfigModel newConfig = new ConfigModel();

        // Run the test
        assertThrows(IOException.class, () -> {
            configServiceUnderTest.updateConfig(newConfig);
        });
    }

    @Test
    void testUpdateConfig_ThrowsInvalidJsonException() {
        // Setup
        final ConfigModel newConfig = new ConfigModel();

        // Run the test
        assertThrows(InvalidJsonException.class, () -> {
            configServiceUnderTest.updateConfig(newConfig);
        });
    }

    @Test
    void testUpdateConfig1() throws Exception {
        // Setup
        final ConfigDto newConfig = new ConfigDto();

        // Run the test
        configServiceUnderTest.updateConfig(newConfig);

        // Verify the results
    }

    @Test
    void testUpdateConfig_ThrowsIOException1() {
        // Setup
        final ConfigDto newConfig = new ConfigDto();

        // Run the test
        assertThrows(IOException.class, () -> {
            configServiceUnderTest.updateConfig(newConfig);
        });
    }

    @Test
    void testUpdateConfig_ThrowsInvalidJsonException1() {
        // Setup
        final ConfigDto newConfig = new ConfigDto();

        // Run the test
        assertThrows(InvalidJsonException.class, () -> {
            configServiceUnderTest.updateConfig(newConfig);
        });
    }

    @Test
    void testUpdateConfig_ThrowsUnavailablePortException1() {
        // Setup
        final ConfigDto newConfig = new ConfigDto();

        // Run the test
        assertThrows(UnavailablePortException.class, () -> {
            configServiceUnderTest.updateConfig(newConfig);
        });
    }

    @Test
    void testGetConfig() {
        // Setup

        // Run the test
        final ConfigModel result = ConfigService.getConfig();

        // Verify the results
    }

    @Test
    void testReloadConfig() throws Exception {
        // Setup

        // Run the test
        ConfigService.reloadConfig();

        // Verify the results
    }

    @Test
    void testReloadConfig_ThrowsFileNotFoundException() {
        // Setup

        // Run the test
        assertThrows(FileNotFoundException.class, () -> {
            ConfigService.reloadConfig();
        });
    }

    @Test
    void testReloadConfig_ThrowsInvalidJsonException() {
        // Setup

        // Run the test
        assertThrows(InvalidJsonException.class, () -> {
            ConfigService.reloadConfig();
        });
    }
}
