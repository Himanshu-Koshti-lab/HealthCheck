package com.syetem.healthcheck.utils;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class CommonUtils {
    public final static Instant StartTime = Instant.now();
}
