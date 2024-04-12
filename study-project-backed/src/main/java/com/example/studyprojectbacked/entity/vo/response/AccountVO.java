package com.example.studyprojectbacked.entity.vo.response;

import lombok.Data;

import java.util.Date;

@Data
public class AccountVO {
    int id;
    String username;
    String email;
    String role;
    String avatar;
    Date register_time;
}
