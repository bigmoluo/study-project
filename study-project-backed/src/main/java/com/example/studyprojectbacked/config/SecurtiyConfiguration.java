package com.example.studyprojectbacked.config;

import com.example.studyprojectbacked.entity.RestBeen;
import com.example.studyprojectbacked.entity.dto.Account;
import com.example.studyprojectbacked.entity.vo.response.AuthorizeVO;
import com.example.studyprojectbacked.filter.JwtAuthorizeFilter;
import com.example.studyprojectbacked.filter.RequestLogFilter;
import com.example.studyprojectbacked.mapper.AccountMapper;
import com.example.studyprojectbacked.util.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.AccessDeniedException;

@Configuration
public class SecurtiyConfiguration {
    @Resource
    JwtUtil jwtUtil;
    @Resource
    JwtAuthorizeFilter jwtAuthorizeFilter;
    @Resource
    AccountMapper accountMapper;
    @Resource
    RequestLogFilter requestLogFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth ->{
                    auth.requestMatchers("/api/auth/**","/error").permitAll();
                    auth.requestMatchers("images/**").permitAll();
                    auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll();
                    auth.anyRequest().authenticated();
                })
                .formLogin(conf -> {
                    conf.loginProcessingUrl("/api/auth/login");
                    conf.successHandler(this::handleProcess);
                    conf.failureHandler(this::handleProcess);
                })
                .logout(conf -> {
                    conf.logoutUrl("/api/auth/logout");
                    conf.logoutSuccessHandler(this::onLogoutSuccess);
                })
                .exceptionHandling(conf -> {
                    conf.authenticationEntryPoint(this::handleProcess);
                    conf.accessDeniedHandler(this::handleProcess);
                })
                .sessionManagement(conf -> {
                    conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .addFilterBefore(requestLogFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthorizeFilter, RequestLogFilter.class)
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
            writer.write(RestBeen.unauthorized(exception.getMessage()).asJsonString());
        } else if (exceptionOrAuthentication instanceof Authentication authentication){
            User user = (User) authentication.getPrincipal();
            Account account = accountMapper.getAccountByUsernameOrEmail(user.getUsername());
            String token = jwtUtil.createJwt(user,account.getId(),account.getUsername());
            AuthorizeVO vo = account.asViewObject(AuthorizeVO.class, v -> {
                v.setExpire(jwtUtil.expireTime());
                v.setToken(token);
            });
            writer.write(RestBeen.success(vo).asJsonString());
        }
    }

    private void onLogoutSuccess(HttpServletRequest request,
                                 HttpServletResponse response,
                                 Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        String authorization = request.getHeader("Authorization");
        if(jwtUtil.invalidateJwt(authorization)){
            writer.write(RestBeen.success("退出登录成功").asJsonString());
        } else {
            writer.write(RestBeen.failure(400,"退出登录失败").asJsonString());
        }
    }
}
