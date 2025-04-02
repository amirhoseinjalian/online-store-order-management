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

/**
 * The RequestTracingFilter class is a servlet filter that assigns a unique trace identifier
 * to each HTTP request processed by the application.
 * <p>
 * This filter generates a random UUID as the trace identifier and sets it as a response header
 * ("X-Request-ID"). It also adds the trace identifier to the MDC (Mapped Diagnostic Context)
 * to facilitate tracking and logging across multiple components handling the same request.
 * </p>
 *
 * @author amirhosein jalian
 */
@Component
public class RequestTracingFilter extends OncePerRequestFilter {

    private static final String TRACE_ID_HEADER = "X-Request-ID";

    /**
     * Filters each incoming HTTP request to add a unique trace identifier.
     * <p>
     * Generates a random UUID, sets it as the value of the "X-Request-ID" response header,
     * and adds it to the MDC for the duration of the request processing. This enables consistent
     * tracing of requests in the application's logs.
     * </p>
     *
     * @param request     the incoming HttpServletRequest.
     * @param response    the outgoing HttpServletResponse.
     * @param filterChain the filter chain to pass the request and response to the next filter.
     * @throws ServletException if an exception occurs during the filter processing.
     * @throws IOException      if an input or output error is detected.
     */
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
