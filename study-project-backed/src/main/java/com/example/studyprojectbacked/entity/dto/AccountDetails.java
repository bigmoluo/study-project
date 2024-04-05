package com.example.studyprojectbacked.entity.dto;

import com.example.studyprojectbacked.entity.BaseData;
import lombok.Data;

@Data
public class AccountDetails implements BaseData {
    int id;
    int gender;
    String phone;
    String qq;
    String wx;
    String desc;
}
