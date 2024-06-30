package com.quran.tester.common.config;

import io.micrometer.context.ContextRegistry;
import io.micrometer.context.ContextSnapshotFactory;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.contextpropagation.ObservationThreadLocalAccessor;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.contextpropagation.ObservationAwareSpanThreadLocalAccessor;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.MDC;
import org.slf4j.event.Level;
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.server.WebFilter;
import org.zalando.logbook.*;
import org.zalando.logbook.core.DefaultCorrelationId;
import org.zalando.logbook.core.DefaultHttpLogFormatter;
import org.zalando.logbook.core.DefaultHttpLogWriter;
import org.zalando.logbook.core.DefaultSink;
import org.zalando.logbook.json.JsonHttpLogFormatter;
import org.zalando.logbook.logstash.LogstashLogbackSink;
import org.zalando.logbook.netty.LogbookServerHandler;
import reactor.netty.Metrics;

import java.util.Optional;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class TracingConfig {

    private final Tracer tracer;
    private final ObservationRegistry observationRegistry;

    @Bean
    public ContextSnapshotFactory contextSnapshotFactory() {
        return ContextSnapshotFactory.builder().build();
    }

    @Bean
    public CorrelationId correlationId() {
        return request -> Optional.ofNullable(tracer.currentSpan())
                .map(span -> span.context().traceId())
                .orElseGet(() -> new DefaultCorrelationId().generate(request));
    }

    @PostConstruct
    public void postConstruct() {
        ContextRegistry.getInstance().registerThreadLocalAccessor(new ObservationAwareSpanThreadLocalAccessor(tracer));
        ObservationThreadLocalAccessor.getInstance().setObservationRegistry(observationRegistry);
        Metrics.observationRegistry(observationRegistry);
    }

    @Bean
    @Profile("!local")
    public Sink logstashSink() {
        return new LogstashLogbackSink(new JsonHttpLogFormatter(), Level.DEBUG);
    }

    @Bean
    @Profile("local")
    public Sink defaultLogbookSink() {
        return new DefaultSink(new DefaultHttpLogFormatter(), new DefaultHttpLogWriter());
    }

    @Bean
    public Logbook logbookServer(CorrelationId correlationId, Sink sink) {
        return Logbook.builder()
                .sink(sink)
                .correlationId(correlationId)
                .headerFilter(_ -> HttpHeaders.empty())
                .bodyFilter((_, _) -> Strings.EMPTY)
                .build();
    }

    @Bean
    public NettyServerCustomizer logbookNettyServerCustomizer(
            ContextSnapshotFactory contextSnapshotFactory,
            Logbook logbookServer
    ) {
        return server -> server.doOnConnection(connection -> connection.addHandlerLast(
                new TracingChannelDuplexHandler(
                        new LogbookServerHandler(logbookServer),
                        contextSnapshotFactory
                )
        )).metrics(true, Function.identity());
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public WebFilter datadogTraceIdRemapFilter(Tracer tracer) {
        return (exchange, chain) -> chain.filter(exchange)
                .contextWrite(ctx -> {
                    Optional.ofNullable(tracer.currentSpan())
                            .ifPresent(span -> MDC.put("dd.trace_id", span.context().traceId()));

                    return ctx;
                }).doFinally(_ -> MDC.remove("dd.trace_id"));
    }

    @RequiredArgsConstructor
    public static class TracingChannelDuplexHandler extends ChannelDuplexHandler {

        private final ChannelDuplexHandler delegate;
        private final ContextSnapshotFactory contextSnapshotFactory;

        @Override
        @SneakyThrows
        public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object msg) {
            try (var _ = contextSnapshotFactory.setThreadLocalsFrom(ctx.channel())) {
                delegate.channelRead(ctx, msg);
            }
        }

        @Override
        @SneakyThrows
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
            try (var _ = contextSnapshotFactory.setThreadLocalsFrom(ctx.channel())) {
                delegate.write(ctx, msg, promise);
            }
        }

        @Override
        public void flush(ChannelHandlerContext ctx) {
            try (var _ = contextSnapshotFactory.setThreadLocalsFrom(ctx.channel())) {
                ctx.flush();
            }
        }
    }
}
