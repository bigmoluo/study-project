package com.example.studyprojectbacked.controller;

import com.example.studyprojectbacked.entity.RestBeen;
import com.example.studyprojectbacked.service.AccountService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {
    @Resource
    AccountService accountService;
    @GetMapping("/ask-code")
    public RestBeen askVerifyCode(@RequestParam String email,
                                  @RequestParam String type,
                                  HttpServletRequest request){
        String message = accountService.registerEmailVerifyCode(type, email, request.getLocalAddr());
        return message == null ? RestBeen.success() : RestBeen.failure(400,message);
    }
}
