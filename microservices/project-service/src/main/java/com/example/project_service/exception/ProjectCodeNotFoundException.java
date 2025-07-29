package com.example.project_service.exception;

public class ProjectCodeNotFoundException extends RuntimeException {
    public ProjectCodeNotFoundException(String message) {
        super(message);
    }
}
