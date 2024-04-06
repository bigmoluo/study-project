package com.example.studyprojectbacked.service;

import com.example.studyprojectbacked.entity.dto.Account;
import com.example.studyprojectbacked.entity.vo.request.*;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends UserDetailsService {
    String registerEmailVerifyCode(String type, String email, String ip);
    String registerEmailAccount(EmailRegisterVO vo);
    String resetConfirm(ResetConfirmVO vo);
    String resetEmailAccountPassword(EmailResetVO vo);
    Account findAccountById(int id);
    String modifyEmailById(int id, ModifyEmailVO vo);

    String changePassword(int id, ChangePasswordVO vo);
}
