package com.syetem.healthcheck.service;

import com.syetem.healthcheck.entity.Application;
import com.syetem.healthcheck.repository.ApplicationRepository;
import com.syetem.healthcheck.response.ApplicationResponse;
import com.syetem.healthcheck.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class HealthMonitor {

    ApplicationRepository applicationRepository;
    public final LocalDateTime now = LocalDateTime.now();
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    HealthMonitor(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
        executor.scheduleWithFixedDelay(this::updateStatus, 10, 10, TimeUnit.SECONDS);
    }

    public List<ApplicationResponse> healthMonitor() {
        return applicationRepository.findAll().stream().map(application -> {
            Duration uptime = Duration.between(CommonUtils.StartTime, Instant.now());
            LocalDateTime uptimeTime = LocalDateTime.ofEpochSecond(
                    uptime.getSeconds(), 0, java.time.ZoneOffset.UTC);
            return ApplicationResponse.builder()
                    .applicationName(application.getApplicationName())
                    .upTime(uptimeTime.toLocalTime().toString())
                    .status(application.isStatus())
                    .id(application.getId())
                    .build();
        }).toList();
    }

    @Transactional
    public void loadData() {

//        Data Cleanup
        applicationRepository.deleteAll();

//        Dummy Application register
        Application application = Application.builder()
                .applicationName("HealthCheck")
                .status(true)
                .upTime(now)
                .build();
        Application application2 = Application.builder()
                .applicationName("LoadBalancerCheck")
                .status(true)
                .upTime(now)
                .build();
        Application application3 = Application.builder()
                .applicationName("RouterCheck")
                .status(true)
                .upTime(LocalDateTime.now().minusSeconds(6000))
                .build();
        Application application4 = Application.builder()
                .applicationName("ProxyCheck")
                .status(false)
                .upTime(now)
                .build();

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/register";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        List.of(application, application2, application3, application4).forEach(app -> {
            HttpEntity<Application> httpEntity = new HttpEntity<>(app, httpHeaders);
            restTemplate.postForEntity(url, httpEntity, String.class);
        });

        log.info("Dummy application registration completed. ");

    }

    public void updateStatus() {

        if (applicationRepository.count() == 0)
            loadData();

        log.info("Refreshing application status...");
        RestTemplate restTemplate = new RestTemplate();
        log.info("Number of Registered applications : " + applicationRepository.count());
        applicationRepository.findAll().forEach(application -> {
            String applicationStatus = restTemplate.getForObject("http://localhost:8081/api/fetchStatus/{applicationName}", String.class, application.getApplicationName());
            log.info("Status for application {} : {}", application.getApplicationName(), applicationStatus);
            assert applicationStatus != null;
            application.setStatus(applicationStatus.equalsIgnoreCase("up"));
            applicationRepository.save(application);
        });
        log.info("Refreshing application status done.");
    }
}
