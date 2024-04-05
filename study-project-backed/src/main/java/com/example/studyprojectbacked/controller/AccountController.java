package com.example.studyprojectbacked.controller;

import com.example.studyprojectbacked.entity.RestBeen;
import com.example.studyprojectbacked.entity.dto.Account;
import com.example.studyprojectbacked.entity.dto.AccountDetails;
import com.example.studyprojectbacked.entity.vo.request.DetailsSaveVO;
import com.example.studyprojectbacked.entity.vo.response.AccountDetailsVO;
import com.example.studyprojectbacked.entity.vo.response.AccountVO;
import com.example.studyprojectbacked.service.AccountDetailsService;
import com.example.studyprojectbacked.service.AccountService;
import com.example.studyprojectbacked.util.Const;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class AccountController {
    @Resource
    AccountService accountService;
    @Resource
    AccountDetailsService accountDetailsService;
    @GetMapping("/info")
    public RestBeen<AccountVO> info(@RequestAttribute(Const.ATTR_USER_ID) int id){
        Account account = accountService.findAccountById(id);
        return RestBeen.success(account.asViewObject(AccountVO.class));
    }

    @GetMapping("/details")
    public RestBeen<AccountDetailsVO> details(@RequestAttribute(Const.ATTR_USER_ID) int id){
        AccountDetails details = Optional
                .ofNullable(accountDetailsService.fintAccountDetailsById(id))
                .orElseGet(AccountDetails::new);
        return RestBeen.success(details.asViewObject(AccountDetailsVO.class));
    }

    @PostMapping("/save-details")
    public RestBeen<Void> saveDetails(@RequestAttribute(Const.ATTR_USER_ID) int id,
                                      @RequestBody @Valid DetailsSaveVO vo){
        boolean success = accountDetailsService.savaAccountDetails(id,vo);
        return success ? RestBeen.success() : RestBeen.failure(400, "此用户名已被其他用户注册，请重新更换！");
    }
}
