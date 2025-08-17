package com.syetem.healthcheck;

import com.syetem.healthcheck.entity.Application;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;

@Registered
public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
