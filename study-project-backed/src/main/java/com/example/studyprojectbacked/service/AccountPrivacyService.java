package com.example.studyprojectbacked.service;

import com.example.studyprojectbacked.entity.dto.AccountPrivacy;
import com.example.studyprojectbacked.entity.vo.request.PrivacySaveVO;

public interface AccountPrivacyService {
    void savePrivacy(int id, PrivacySaveVO vo);
    AccountPrivacy getAccountPrivacy(int id);
}
