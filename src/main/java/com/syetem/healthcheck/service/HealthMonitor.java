package com.syetem.healthcheck.service;

import com.syetem.healthcheck.ApplicationRepository;
import com.syetem.healthcheck.Entity.Application;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HealthMonitor {
    static ApplicationRepository applicationRepository;

    HealthMonitor(ApplicationRepository applicationRepository) {
        HealthMonitor.applicationRepository = applicationRepository;
    }

    static final LocalDateTime now = LocalDateTime.now();


    public List<Application> loadData() {
        Application application = Application.builder()
                .ApplicationName("Health Check")
                .status(true)
                .UpTime(now)
                .build();
        Application application2 = Application.builder()
                .ApplicationName("Load Balancer Check")
                .status(true)
                .UpTime(now)
                .build();
        Application application3 = Application.builder()
                .ApplicationName("Router Check")
                .status(true)
                .UpTime(LocalDateTime.now().minusSeconds(now.getNano()))
                .build();
        Application application4 = Application.builder()
                .ApplicationName("Proxy Check")
                .status(false)
                .UpTime(now)
                .build();

        applicationRepository.saveAll(
                List.of(
                        application,
                        application2,
                        application3,
                        application4));


        return applicationRepository.findAll();
    }

}
