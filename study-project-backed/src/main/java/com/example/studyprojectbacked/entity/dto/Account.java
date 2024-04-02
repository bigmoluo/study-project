package com.example.studyprojectbacked.entity.dto;

import com.example.studyprojectbacked.entity.BaseData;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Account implements BaseData {
    Integer id;
    String email;
    String username;
    String password;
    String role;
    Date register_time;
}
