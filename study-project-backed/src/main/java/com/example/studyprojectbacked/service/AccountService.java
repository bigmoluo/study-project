package com.example.studyprojectbacked.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends UserDetailsService {
    String registerEmailVerifyCode(String type, String email, String ip);
}
