package com.syetem.healthcheck.service;

import com.syetem.healthcheck.ApplicationRepository;
import com.syetem.healthcheck.entity.Application;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class HealthMonitor {
    ApplicationRepository applicationRepository;
    static final LocalDateTime now = LocalDateTime.now();


    HealthMonitor(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
        loadData();
    }


    public List<Application> healthMonitor() {
        return applicationRepository.findAll();
    }

    public void loadData() {

        if (applicationRepository.count() != 0)
            log.info("Application already exists");

        Application application = Application.builder()
                .applicationName("Health Check")
                .status(true)
                .upTime(now)
                .build();
        Application application2 = Application.builder()
                .applicationName("Load Balancer Check")
                .status(true)
                .upTime(now)
                .build();
        Application application3 = Application.builder()
                .applicationName("Router Check")
                .status(true)
                .upTime(LocalDateTime.now().minusSeconds(6000))
                .build();
        Application application4 = Application.builder()
                .applicationName("Proxy Check")
                .status(false)
                .upTime(now)
                .build();

        applicationRepository.saveAll(
                List.of(application, application2, application3, application4));

        log.info("Data loading completed. ");
    }

}
