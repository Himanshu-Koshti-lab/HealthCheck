package com.syetem.healthcheck.repository;

import com.syetem.healthcheck.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Application findByApplicationName(String applicationName);
}
