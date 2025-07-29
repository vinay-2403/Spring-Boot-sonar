package com.example.employee_service.feign;

import com.example.employee_service.dto.ProjectDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "project-service")
public interface ProjectClient {

    @GetMapping("/projects/code/{code}")
    ProjectDTO getProjectByCode(@PathVariable("code") String code);
}


