package com.example.studyprojectbacked.entity.dto;

import com.example.studyprojectbacked.entity.BaseData;
import lombok.Data;

import java.util.Date;

@Data
public class Account implements BaseData {
    int id;
    String email;
    String username;
    String password;
    String role;
    Date register_time;
}
