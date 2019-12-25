package com.nickng.ccd.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComparisionServiceTest {

    private ComparisionService comparisionServiceUnderTest;

    @BeforeEach
    void setUp() {
        comparisionServiceUnderTest = new ComparisionService();
    }

    @Test
    void testGetDifference() {
        // Setup

        // Run the test
        final String result = comparisionServiceUnderTest.getDifference("template", "comparedText");

        // Verify the results
        assertEquals("result", result);
    }
}
