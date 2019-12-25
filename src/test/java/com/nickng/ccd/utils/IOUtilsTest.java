package com.nickng.ccd.utils;

import com.nickng.ccd.exception.FileNotExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IOUtilsTest {

    private IOUtils ioUtilsUnderTest;

    @BeforeEach
    void setUp() {
        ioUtilsUnderTest = new IOUtils();
    }

    @Test
    void testGetLastestFileContent() throws Exception {
        // Setup

        // Run the test
        final String result = ioUtilsUnderTest.getLastestFileContent("title");

        // Verify the results
        assertEquals("result", result);
    }

    @Test
    void testGetLastestFileContent_ThrowsIOException() {
        // Setup

        // Run the test
        assertThrows(IOException.class, () -> {
            ioUtilsUnderTest.getLastestFileContent("title");
        });
    }

    @Test
    void testGetLastestFileContent_ThrowsFileNotExistException() {
        // Setup

        // Run the test
        assertThrows(FileNotExistException.class, () -> {
            ioUtilsUnderTest.getLastestFileContent("title");
        });
    }

    @Test
    void testStoreFileAsOldVersion() throws Exception {
        // Setup

        // Run the test
        ioUtilsUnderTest.storeFileAsOldVersion("title");

        // Verify the results
    }

    @Test
    void testStoreFileAsOldVersion_ThrowsIOException() {
        // Setup

        // Run the test
        assertThrows(IOException.class, () -> {
            ioUtilsUnderTest.storeFileAsOldVersion("title");
        });
    }

    @Test
    void testStoreFileAsLatest() {
        // Setup

        // Run the test
        ioUtilsUnderTest.storeFileAsLatest("title", "content");

        // Verify the results
    }

    @Test
    void testStoreFileAsDifference() {
        // Setup
        final Path expectedResult = Paths.get("filename.txt");

        // Run the test
        final Path result = ioUtilsUnderTest.storeFileAsDifference("title", "content");

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void testGetFilePath() throws Exception {
        assertEquals("result", IOUtils.getFilePath("extension", "dirs"));
        assertThrows(IOException.class, () -> {
            IOUtils.getFilePath("extension", "dirs");
        });
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void testGetConfigFilePath() throws Exception {
        assertEquals("result", IOUtils.getConfigFilePath());
        assertThrows(IOException.class, () -> {
            IOUtils.getConfigFilePath();
        });
    }

    @Test
    void testSaveProperty() throws Exception {
        // Setup

        // Run the test
        IOUtils.saveProperty("property", "value");

        // Verify the results
    }

    @Test
    void testSaveProperty_ThrowsIOException() {
        // Setup

        // Run the test
        assertThrows(IOException.class, () -> {
            IOUtils.saveProperty("property", "value");
        });
    }

    @Test
    void testInitDirectories() throws Exception {
        // Setup

        // Run the test
        IOUtils.initDirectories();

        // Verify the results
    }

    @Test
    void testInitDirectories_ThrowsIOException() {
        // Setup

        // Run the test
        assertThrows(IOException.class, () -> {
            IOUtils.initDirectories();
        });
    }

    @Test
    void testCreateDirIfNotExist() throws Exception {
        // Setup
        final Path path = Paths.get("filename.txt");

        // Run the test
        IOUtils.createDirIfNotExist(path);

        // Verify the results
    }

    @Test
    void testCreateDirIfNotExist_ThrowsIOException() {
        // Setup
        final Path path = Paths.get("filename.txt");

        // Run the test
        assertThrows(IOException.class, () -> {
            IOUtils.createDirIfNotExist(path);
        });
    }
}
