package com.example.studyprojectbacked.entity.dto;

import com.example.studyprojectbacked.entity.BaseData;
import com.example.studyprojectbacked.entity.RestBeen;
import lombok.Data;

@Data
public class AccountPrivacy implements BaseData {
    final int id;
    boolean phone = true;
    boolean email = true;
    boolean qq = true;
    boolean wx = true;
    boolean gender = true;
}
