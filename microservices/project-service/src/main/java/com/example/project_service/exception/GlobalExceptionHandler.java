package com.example.project_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProjectCodeNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(ProjectCodeNotFoundException ex){
        Map<String,Object> body=new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error","Not Found");
        body.put("message",ex.getMessage());
        return new ResponseEntity<>(body,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundId(ProjectNotFoundException ex){
        Map<String,Object> body=new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error","Not Found");
        body.put("message",ex.getMessage());
        return new ResponseEntity<>(body,HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object>handleGeneral(Exception ex){
        Map<String,Object> body=new HashMap<>();
        body.put("timeStamp",LocalDateTime.now());
        body.put("status",HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error","Internal server error");
        body.put("message",ex.getMessage());
        return new ResponseEntity<>(body,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
