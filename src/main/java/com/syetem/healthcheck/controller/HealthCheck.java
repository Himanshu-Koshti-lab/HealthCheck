package com.syetem.healthcheck.controller;

import com.syetem.healthcheck.service.HealthMonitor;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {

    HealthMonitor healthMonitor;

    public HealthCheck(HealthMonitor healthMonitor) {
        this.healthMonitor = healthMonitor;
    }

}
