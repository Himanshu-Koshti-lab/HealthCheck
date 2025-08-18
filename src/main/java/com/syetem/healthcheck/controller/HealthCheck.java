package com.syetem.healthcheck.controller;

import com.syetem.healthcheck.repository.ApplicationRepository;
import com.syetem.healthcheck.entity.Application;
import com.syetem.healthcheck.service.HealthMonitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class HealthCheck {

    HealthMonitor healthMonitor;

    ApplicationRepository applicationRepository;

    public HealthCheck(HealthMonitor healthMonitor, ApplicationRepository applicationRepository) {
        this.healthMonitor = healthMonitor;
        this.applicationRepository = applicationRepository;
    }

    @PostMapping("/register")
    public String register(@RequestBody Application application) {
        if (applicationRepository.findByApplicationName(application.getApplicationName()) != null)
            return "Application already registered";
        applicationRepository.save(application);
        return "Registered successfully for application " + application.getApplicationName();
    }


    @GetMapping("/api/fetchStatus/{applicationName}")
    public String fetchStatus(@PathVariable String applicationName) {
        log.info("Fetching status for application {}", applicationName);
        return "up";
    }

}
