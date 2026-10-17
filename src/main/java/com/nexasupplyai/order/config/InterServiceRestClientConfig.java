package com.nexasupplyai.order.config;

import org.slf4j.MDC;
import org.springframework.boot.restclient.RestClientCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.UUID;

@Configuration
public class InterServiceRestClientConfig {

    @Bean
    RestClientCustomizer configurarClientes(InterServiceAuthenticationProvider authenticationProvider,
                                             @Value("${clients.http.connect-timeout:3s}") Duration connectTimeout,
                                             @Value("${clients.http.read-timeout:10s}") Duration readTimeout) {
        HttpClient httpClient = HttpClient.newBuilder().connectTimeout(connectTimeout).build();
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(readTimeout);
        return builder -> builder.requestInterceptor((request, body, execution) -> {
            String correlationId = MDC.get(CorrelationIdFilter.MDC_KEY);
            request.getHeaders().set(CorrelationIdFilter.HEADER,
                    correlationId == null ? UUID.randomUUID().toString() : correlationId);
            authenticationProvider.apply(request.getHeaders());
            return execution.execute(request, body);
        }).requestFactory(requestFactory);
    }
}
