package com.example.studyprojectbacked.entity.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ChangePasswordVO {
    @Length(max = 20,min = 6)
    String password;
    @Length(min = 6,max = 20)
    String new_password;
}
