package com.syetem.healthcheck.controller;

import com.syetem.healthcheck.Entity.Application;
import com.syetem.healthcheck.service.HealthMonitor;
import jdk.jfr.Registered;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/api")
public class HealthCheck {

    HealthMonitor healthMonitor;
    public HealthCheck(HealthMonitor healthMonitor) {
        this.healthMonitor = healthMonitor;
    }

    @GetMapping("/getAppStatus")
    public List<Application> healthCheck() {
        return healthMonitor.loadData();
    }

}
