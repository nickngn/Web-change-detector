package com.nickng.ccd.service;

import com.nickng.ccd.exception.URLException;
import com.nickng.ccd.model.LinkModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.MockitoAnnotations.initMocks;

class ClientServiceTest {

    @Mock
    private RestTemplate mockRestTemplate;

    private ClientService clientServiceUnderTest;

    @BeforeEach
    void setUp() {
        initMocks(this);
        clientServiceUnderTest = new ClientService(mockRestTemplate);
    }

    @Test
    void testCallRequest() throws Exception {
        // Setup
        final LinkModel link = new LinkModel();

        // Run the test
        final String result = clientServiceUnderTest.callRequest(link);

        // Verify the results
        assertEquals("result", result);
    }

    @Test
    void testCallRequest_ThrowsURLException() {
        // Setup
        final LinkModel link = new LinkModel();

        // Run the test
        assertThrows(URLException.class, () -> {
            clientServiceUnderTest.callRequest(link);
        });
    }
}
