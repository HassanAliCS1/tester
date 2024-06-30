package com.quran.tester.config;

import io.micrometer.tracing.Tracer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TracingConfig {

    @Bean
    public Tracer tracer() {
        return Tracer.NOOP;
    }
}
