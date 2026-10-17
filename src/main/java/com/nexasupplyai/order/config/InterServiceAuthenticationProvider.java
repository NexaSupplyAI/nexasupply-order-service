package com.nexasupplyai.order.config;

import org.springframework.http.HttpHeaders;

public interface InterServiceAuthenticationProvider {
    void apply(HttpHeaders headers);
}
