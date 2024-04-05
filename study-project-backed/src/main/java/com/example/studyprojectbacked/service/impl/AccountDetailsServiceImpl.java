package com.example.studyprojectbacked.service.impl;

import com.example.studyprojectbacked.entity.dto.Account;
import com.example.studyprojectbacked.entity.dto.AccountDetails;
import com.example.studyprojectbacked.entity.vo.request.DetailsSaveVO;
import com.example.studyprojectbacked.mapper.AccountDetailsMapper;
import com.example.studyprojectbacked.mapper.AccountMapper;
import com.example.studyprojectbacked.service.AccountDetailsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class AccountDetailsServiceImpl implements AccountDetailsService {
    @Resource
    AccountDetailsMapper accountDetailsMapper;
    @Resource
    AccountMapper accountMapper;
    @Override
    public AccountDetails fintAccountDetailsById(int id) {
        return accountDetailsMapper.findAccountDetailsById(id);
    }

    @Override
    public synchronized boolean savaAccountDetails(int id, DetailsSaveVO vo) {
        Account account = accountMapper.getAccountByUsernameOrEmail(vo.getUsername());
        if (account == null || account.getId() == id){
            AccountDetails accountDetails = this.asAccountDetails(id,vo);
            if (accountDetailsMapper.findAccountDetailsById(id) == null){
                accountDetailsMapper.saveAccountDetails(accountDetails);
            } else {
                accountDetailsMapper.updateAccountDetails(id, accountDetails);
            }
            accountMapper.updateAccountUsernameByUsername(id, vo.getUsername());
            return true;
        }
        return false;
    }

    private AccountDetails asAccountDetails(int id,DetailsSaveVO vo){
        AccountDetails accountDetails = new AccountDetails();
        accountDetails.setId(id);
        accountDetails.setGender(vo.getGender());
        accountDetails.setPhone(vo.getPhone());
        accountDetails.setQq(vo.getQq());
        accountDetails.setWx(vo.getWx());
        accountDetails.setDesc(vo.getDesc());
        return accountDetails;
    }
}
