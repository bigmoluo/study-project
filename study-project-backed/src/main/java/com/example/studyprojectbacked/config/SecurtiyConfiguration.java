package com.example.studyprojectbacked.config;

import com.example.studyprojectbacked.entity.RestBeen;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.AccessDeniedException;

@Configuration
public class SecurtiyConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth ->{
                    auth.anyRequest().authenticated();
                })
                .formLogin(conf -> {
                    conf.loginProcessingUrl("/api/auth/login");
                    conf.successHandler(this::handleProcess);
                    conf.failureHandler(this::handleProcess);
                })
                .logout(conf -> {
                    conf.logoutUrl("api/auth/logout");
                })
                .exceptionHandling(conf -> {
                    conf.authenticationEntryPoint(this::handleProcess);
                })
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    private void handleProcess(HttpServletRequest request,
                               HttpServletResponse response,
                               Object exceptionOrAuthentication) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        if(exceptionOrAuthentication instanceof AccessDeniedException exception){
            writer.write(RestBeen.failure(404,exception.getMessage()).asJsonString());
        } else if (exceptionOrAuthentication instanceof Exception exception) {
            writer.write(RestBeen.failure(401,exception.getMessage()).asJsonString());
        } else if (exceptionOrAuthentication instanceof Authentication authentication){
            writer.write(RestBeen.success(authentication.getName()).asJsonString());
        }
    }
}
