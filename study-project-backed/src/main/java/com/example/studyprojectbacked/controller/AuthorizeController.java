package com.example.studyprojectbacked.controller;

import com.example.studyprojectbacked.entity.RestBeen;
import com.example.studyprojectbacked.entity.vo.request.EmailRegisterVO;
import com.example.studyprojectbacked.entity.vo.request.EmailResetVO;
import com.example.studyprojectbacked.entity.vo.request.ResetConfirmVO;
import com.example.studyprojectbacked.service.AccountService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;
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
        return this.messageHandle(vo, accountService::registerEmailAccount);
    }
    @PostMapping("/reset_confirm")
    public RestBeen<Void> resetConfirm(@RequestBody @Valid ResetConfirmVO vo){
        return this.messageHandle(vo, accountService::resetConfirm);
    }
    @PostMapping("/reset_password")
    public RestBeen<Void> resetConfirmPassword(@RequestBody @Valid EmailResetVO vo){
        return this.messageHandle(vo, accountService::resetEmailAccountPassword);
    }

    private <T> RestBeen<Void> messageHandle(T vo, Function<T, String> function){
        return this.messageHandle(() -> function.apply(vo));
    }

    private RestBeen<Void> messageHandle(Supplier<String> action){
        String message = action.get();
        return message == null ? RestBeen.success() : RestBeen.failure(400, message);
    }
}
