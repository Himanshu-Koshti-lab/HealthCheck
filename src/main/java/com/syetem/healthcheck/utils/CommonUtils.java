package com.syetem.healthcheck.utils;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@Component
public class CommonUtils {
    public final static Instant StartTime = Instant.now();


    public final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public final static String LOCAL_DATE_TIME = ZonedDateTime.now().format(DATE_TIME_FORMATTER);
    public final static String ZONED_DATE_TIME = ZonedDateTime.now(ZoneOffset.UTC).format(DATE_TIME_FORMATTER);



    public final static DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

}
