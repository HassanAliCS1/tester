package com.quran.tester.quranTester.quranCloud.Config;

import com.quran.tester.common.config.TracingConfig;
import io.micrometer.context.ContextSnapshotFactory;
import io.netty.channel.ChannelOption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.zalando.logbook.*;
import org.zalando.logbook.core.BodyFilters;
import org.zalando.logbook.json.PrettyPrintingJsonBodyFilter;
import org.zalando.logbook.netty.LogbookClientHandler;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@Configuration
public class QuranCloudWebClientConfig {


    @Bean
    public BodyFilter jsonFormatBodyFilter(Environment env) {
        return env.getActiveProfiles()[0].equals("local") ?
                new PrettyPrintingJsonBodyFilter() : BodyFilters.defaultValue();
    }

    @Bean
    public Logbook logbookQuranCloud(CorrelationId correlationId, Sink sink, BodyFilter jsonFormatBodyFilter) {
        return Logbook.builder()
                .sink(sink)
                .correlationId(correlationId)
                .headerFilter(_ -> HttpHeaders.empty())
                .bodyFilter(jsonFormatBodyFilter)
                .build();
    }

    @Bean
    public WebClient quranCloudWebClient(
            WebClient.Builder builder,
            QuranCloudProperties properties,
            ContextSnapshotFactory contextSnapshotFactory,
            Logbook logbookQuranCloud
    ) {
        HttpClient httpClient = HttpClient
                .newConnection()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .responseTimeout(Duration.ofSeconds(20))
                .doOnConnected(connection -> connection.addHandlerLast(
                        new TracingConfig.TracingChannelDuplexHandler(
                                new LogbookClientHandler(logbookQuranCloud),
                                contextSnapshotFactory
                        )
                ));

        return builder
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(properties.getUrl())
                .defaultHeader(ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
