package com.example.studyprojectbacked.service.impl;

import com.example.studyprojectbacked.entity.dto.AccountPrivacy;
import com.example.studyprojectbacked.entity.vo.request.PrivacySaveVO;
import com.example.studyprojectbacked.mapper.AccountPrivacyMapper;
import com.example.studyprojectbacked.service.AccountPrivacyService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class AccountPrivacyServiceImpl implements AccountPrivacyService {
    @Resource
    AccountPrivacyMapper privacyMapper;
    @Override
    public void savePrivacy(int id, PrivacySaveVO vo) {
        boolean existAccountPrivacy = true;
        AccountPrivacy privacy = privacyMapper.getAccountPrivacyById(id);
        if (privacy == null) {
            existAccountPrivacy = false;
            privacy = new AccountPrivacy(id);
        }
        boolean status = vo.isStatus();
        switch (vo.getType()) {
            case "phone" -> privacy.setPhone(status);
            case "email" -> privacy.setEmail(status);
            case "gender" -> privacy.setGender(status);
            case "wx" -> privacy.setWx(status);
            case "qq" -> privacy.setQq(status);
        }
        if ( existAccountPrivacy ){
            privacyMapper.updateAccountPrivacyById(id, privacy);
        } else {
            privacyMapper.saveAccountPrivacy(privacy);
        }
    }

    @Override
    public AccountPrivacy getAccountPrivacy(int id) {
        return Optional.ofNullable(privacyMapper.getAccountPrivacyById(id)).orElse(new AccountPrivacy(id));
    }
}
