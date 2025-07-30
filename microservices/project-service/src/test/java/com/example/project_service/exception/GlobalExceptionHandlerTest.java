package com.example.project_service.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleProjectCodeNotFoundException() {
        ProjectCodeNotFoundException ex = new ProjectCodeNotFoundException("Code not found");
        ResponseEntity<Object> response = handler.handleNotFound(ex);

        assertEquals(404, response.getStatusCodeValue());

        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertNotNull(body);
        assertEquals("Not Found", body.get("error"));
        assertEquals("Code not found", body.get("message"));
    }

    @Test
    void testHandleProjectNotFoundException() {
        ProjectNotFoundException ex = new ProjectNotFoundException("Project not found");
        ResponseEntity<Object> response = handler.handleNotFoundId(ex);

        assertEquals(404, response.getStatusCodeValue());

        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertNotNull(body);
        assertEquals("Not Found", body.get("error"));
        assertEquals("Project not found", body.get("message"));
    }

    @Test
    void testHandleGeneralException() {
        Exception ex = new Exception("Something went wrong");
        ResponseEntity<Object> response = handler.handleGeneral(ex);

        assertEquals(500, response.getStatusCodeValue());

        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertNotNull(body);
        assertEquals("Internal server error", body.get("error"));
        assertEquals("Something went wrong", body.get("message"));
    }
}
