package com.nexasupplyai.order.config;

import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Duration;
import java.util.Locale;

@Component
public class EntraInterServiceAuthenticationProvider implements InterServiceAuthenticationProvider {
    private final String mode;
    private final String scope;
    private final DefaultAzureCredential credential;

    public EntraInterServiceAuthenticationProvider(
            @Value("${clients.authentication.mode:propagate}") String mode,
            @Value("${clients.authentication.scope:}") String scope) {
        this.mode = mode.toLowerCase(Locale.ROOT);
        this.scope = scope;
        this.credential = new DefaultAzureCredentialBuilder().build();
    }

    @Override
    public void apply(HttpHeaders headers) {
        switch (mode) {
            case "none" -> { }
            case "propagate" -> propagate(headers);
            case "managed-identity" -> managedIdentity(headers);
            default -> throw new IllegalStateException("INTER_SERVICE_AUTH_MODE no soportado: " + mode);
        }
    }

    private void propagate(HttpHeaders headers) {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
            HttpServletRequest request = attributes.getRequest();
            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorization != null && !authorization.isBlank()) headers.set(HttpHeaders.AUTHORIZATION, authorization);
        }
    }

    private void managedIdentity(HttpHeaders headers) {
        if (scope == null || scope.isBlank()) {
            throw new IllegalStateException("AZURE_INTER_SERVICE_SCOPE es obligatorio con managed-identity");
        }
        var token = credential.getToken(new TokenRequestContext().addScopes(scope)).block(Duration.ofSeconds(10));
        if (token == null) throw new IllegalStateException("No se obtuvo token Entra para la llamada interservicio");
        headers.setBearerAuth(token.getToken());
    }
}
