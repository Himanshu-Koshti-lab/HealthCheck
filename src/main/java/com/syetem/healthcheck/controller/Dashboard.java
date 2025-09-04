package com.syetem.healthcheck.controller;

import com.syetem.healthcheck.response.ApplicationResponse;
import com.syetem.healthcheck.service.HealthMonitor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Dashboard {

    HealthMonitor healthMonitor;

    Dashboard(HealthMonitor healthMonitor) {
        this.healthMonitor = healthMonitor;
    }

    @GetMapping("/applications")
    public String list(Model model) {
        model.addAttribute("apps", healthMonitor.healthMonitor());
        boolean overallUp = healthMonitor.healthMonitor().stream().allMatch(ApplicationResponse::isStatus);
        model.addAttribute("overallUp", overallUp);
        return "list";
    }
}
