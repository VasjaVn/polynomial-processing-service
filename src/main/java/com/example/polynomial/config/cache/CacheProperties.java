package com.example.polynomial.config.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Data
@Component
@ConfigurationProperties("app.cache")
public class CacheProperties {

    private Integer ttl;

    private String timeUnit;

    public TimeUnit getTimeUnit() {
        return switch (timeUnit) {
            case "NANOSECONDS" -> TimeUnit.NANOSECONDS;
            case "MICROSECONDS" -> TimeUnit.MICROSECONDS;
            case "MILLISECONDS" -> TimeUnit.MILLISECONDS;
//            case "SECONDS" -> TimeUnit.SECONDS;
            case "MINUTES" -> TimeUnit.MINUTES;
            case "HOURS" -> TimeUnit.HOURS;
            case "DAYS" -> TimeUnit.DAYS;
            default -> TimeUnit.SECONDS;
        };
    }
}
