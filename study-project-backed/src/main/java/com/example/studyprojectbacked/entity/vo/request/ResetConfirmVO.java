package com.example.studyprojectbacked.entity.vo.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
public class ResetConfirmVO {
    @Email
    String email;
    @Length( max = 6, min = 6)
    String code;
}
