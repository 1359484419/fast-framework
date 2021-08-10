package com.bix.config.filter;

import com.bix.bixApi.utils.TraceIdGenerator;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

@Component
public class TraceIdFilter implements Filter {

    public static final String TRACE_ID = "traceId";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 放入traceId
        String traceId = TraceIdGenerator.getTraceId();
        MDC.put(TRACE_ID, traceId);
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
