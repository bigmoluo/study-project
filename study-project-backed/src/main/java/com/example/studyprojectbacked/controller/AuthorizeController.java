package com.example.studyprojectbacked.controller;

import com.example.studyprojectbacked.entity.RestBeen;
import com.example.studyprojectbacked.entity.vo.request.EmailRegisterVO;
import com.example.studyprojectbacked.entity.vo.request.EmailResetVO;
import com.example.studyprojectbacked.entity.vo.request.ResetConfirmVO;
import com.example.studyprojectbacked.service.AccountService;
import com.example.studyprojectbacked.util.ControllerUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {
    @Resource
    AccountService accountService;
    @Resource
    ControllerUtils utils;
    @GetMapping("/ask-code")
    public RestBeen<Void> askVerifyCode(@RequestParam @Email String email,
                                  @RequestParam @Pattern(regexp = "(register|reset|modify)") String type,
                                  HttpServletRequest request){
        return utils.messageHandle(() ->
                accountService.registerEmailVerifyCode(type, email, request.getLocalAddr()));
    }
    @PostMapping("/register")
    public RestBeen<Void> register(@RequestBody @Valid EmailRegisterVO vo){
        return utils.messageHandle(() -> accountService.registerEmailAccount(vo));
    }
    @PostMapping("/reset_confirm")
    public RestBeen<Void> resetConfirm(@RequestBody @Valid ResetConfirmVO vo){
        return utils.messageHandle(() -> accountService.resetConfirm(vo));
    }
    @PostMapping("/reset_password")
    public RestBeen<Void> resetConfirmPassword(@RequestBody @Valid EmailResetVO vo){
        return utils.messageHandle(() -> accountService.resetEmailAccountPassword(vo));
    }


}
