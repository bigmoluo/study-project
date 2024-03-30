package com.example.studyprojectbacked.mapper;

import com.example.studyprojectbacked.entity.dto.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from db_account where username = #{text} or email = #{text}")
    Account getAccountByUsernameOrEmail(String text);
}
