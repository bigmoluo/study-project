package com.example.studyprojectbacked.controller;

import com.example.studyprojectbacked.entity.RestBeen;
import com.example.studyprojectbacked.entity.vo.request.EmailRegisterVO;
import com.example.studyprojectbacked.service.AccountService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.function.Supplier;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {
    @Resource
    AccountService accountService;
    @GetMapping("/ask-code")
    public RestBeen<Void> askVerifyCode(@RequestParam @Email String email,
                                  @RequestParam @Pattern(regexp = "(register|reset)") String type,
                                  HttpServletRequest request){
        return this.messageHandle(() ->
            accountService.registerEmailVerifyCode(type, email, request.getLocalAddr()));
    }

    @PostMapping("/register")
    public RestBeen<Void> register(@RequestBody @Valid EmailRegisterVO vo){
        return this.messageHandle(() -> accountService.registerEmailAccount(vo));
    }

    private RestBeen<Void> messageHandle(Supplier<String> action){
        String message = action.get();
        return message == null ? RestBeen.success() : RestBeen.failure(400, message);
    }
}
