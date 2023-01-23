package com.kezbek.transaction.config;

import com.kezbek.transaction.model.request.ContextRequest;
import com.kezbek.transaction.model.response.SessionResponse;
import com.kezbek.transaction.service.OnboardService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private OnboardService onboardService;

    private static final String [] ignorePath = {
            "/authorization",
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/swagger-ui",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**",
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.addFilterBefore(serviceFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .mvcMatchers("/swagger-ui.html/**", "/configuration/**", "/swagger-resources/**", "/v2/api-docs", "/webjars/**");
    }

    private Filter serviceFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                Boolean isSkipped = Arrays.stream(ignorePath).filter(url -> request.getRequestURI().equals("/") ||
                                request.getRequestURI().contains(url))
                        .findAny().isPresent();
                if (isSkipped) {
                    log.info("isSkipped [{}, {}]", isSkipped, request.getRequestURI());
                    SecurityContextHolder.getContext().setAuthentication(null);
                    filterChain.doFilter(request, response);
                    return;
                } else {
                    if(StringUtils.isEmpty(request.getHeader(HttpHeaders.AUTHORIZATION))) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized access to API");
                        return;
                    }
                    String token = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1];
                    try {
                        SessionResponse sessionResponse = onboardService.userInfo(ContextRequest.builder()
                                .token(token)
                                .build());
                        request.setAttribute("x-partner-code", sessionResponse.getCompanyCode());
                        filterChain.doFilter(request, response);
                        return;
                    }
                    catch(ResponseStatusException ex) {
                        response.sendError(ex.getStatus().value(), ex.getMessage());
                    }

                }
            }
        };
    }

}
