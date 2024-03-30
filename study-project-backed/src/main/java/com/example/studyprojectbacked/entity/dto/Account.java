package com.example.studyprojectbacked.entity.dto;

import lombok.Data;

import java.util.Date;

@Data
public class Account {
    int id;
    String email;
    String username;
    String password;
    String role;
    Date registerTime;
}
