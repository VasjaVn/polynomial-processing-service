package com.example.polynomial.config.app.props;

import com.example.polynomial.model.domain.CacheWarmData;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Data
@Component
@ConfigurationProperties("app.cache")
public class CacheProperties {

    private boolean enableCache;

    private Integer ttl;

    private String timeUnit;

    private boolean warm;

    private List<CacheWarmData> warmData;

    public TimeUnit getTimeUnit() {
        return switch (timeUnit) {
            case "NANOSECONDS" -> TimeUnit.NANOSECONDS;
            case "MICROSECONDS" -> TimeUnit.MICROSECONDS;
            case "MILLISECONDS" -> TimeUnit.MILLISECONDS;
            case "MINUTES" -> TimeUnit.MINUTES;
            case "HOURS" -> TimeUnit.HOURS;
            case "DAYS" -> TimeUnit.DAYS;
            default -> TimeUnit.SECONDS;
        };
    }
}
