package com.example.studyprojectbacked.service;

import com.example.studyprojectbacked.entity.dto.AccountDetails;
import com.example.studyprojectbacked.entity.vo.request.DetailsSaveVO;

public interface AccountDetailsService {
    AccountDetails fintAccountDetailsById(int id);
    boolean savaAccountDetails(int id, DetailsSaveVO vo);
}
