package com.example.studyprojectbacked.entity.vo.response;

import lombok.Data;

import java.util.Date;

@Data
public class AccountVO {
    String username;
    String email;
    String role;
    Date register_time;
}
