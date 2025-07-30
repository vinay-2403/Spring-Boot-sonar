package com.example.employee_service.exception;

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
    void testHandleEmployeeNotFoundException() {
        EmployeeNotFoundException ex = new EmployeeNotFoundException("Employee not found");
        ResponseEntity<Object> response = handler.handleNotFound(ex);

        assertEquals(404, response.getStatusCodeValue());

        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertNotNull(body);
        assertEquals("Not Found", body.get("error"));
        assertEquals("Employee not found", body.get("message"));
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
