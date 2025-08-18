package com.syetem.healthcheck.service;

import com.syetem.healthcheck.ApplicationRepository;
import com.syetem.healthcheck.entity.Application;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@EnableScheduling
public class HealthMonitor {
    static ApplicationRepository applicationRepository;
    static final LocalDateTime now = LocalDateTime.now();


    HealthMonitor(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }


    public List<Application> healthMonitor() {
        return applicationRepository.findAll();
    }

    @Transactional
    public void loadData() {

        applicationRepository.deleteAll();

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

        log.info("Data loading completed. ");
    }


    //    @Scheduled(fixedRate = 10000)
    public void updateStatus() {

        log.info("Refreshing application status...");
        RestTemplate restTemplate = new RestTemplate();
        log.info("Number of Registered applications : " + applicationRepository.count());
        applicationRepository.findAll().forEach(application -> {
            log.info("Checking status for application : {}", application.getApplicationName());
            String applicationStatus = restTemplate.getForObject("http://localhost:8080/api/fetchStatus/{applicationName}", String.class, application.getApplicationName());
            log.info("Status for application {} : {}", application.getApplicationName(), applicationStatus);
            assert applicationStatus != null;
            application.setStatus(applicationStatus.equalsIgnoreCase("up"));
            applicationRepository.save(application);
        });
        log.info("Refreshing application status done.");
    }
}
