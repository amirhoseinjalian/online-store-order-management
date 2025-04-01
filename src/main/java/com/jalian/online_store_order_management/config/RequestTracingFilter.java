package com.jalian.online_store_order_management.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestTracingFilter extends OncePerRequestFilter {

    private static final String TRACE_ID_HEADER = "X-Request-ID";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        var traceId = UUID.randomUUID().toString();

        response.setHeader(TRACE_ID_HEADER, traceId);

        try (var ignored = MDC.putCloseable("traceId", traceId)) {
            filterChain.doFilter(request, response);
        }
    }
}