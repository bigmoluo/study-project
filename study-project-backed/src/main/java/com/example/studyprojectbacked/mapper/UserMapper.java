package com.example.studyprojectbacked.mapper;

import com.example.studyprojectbacked.entity.BaseData;
import com.example.studyprojectbacked.entity.dto.Account;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("select * from db_account where username = #{text} or email = #{text}")
    Account getAccountByUsernameOrEmail(String text);

    @Insert("insert into db_account(email,username,password,role,register_time) " +
            "values (#{email},#{username},#{password},#{role},#{register_time})")
    Boolean saveAccount(Account account);

    @Update("update db_account set password = #{password} where email = #{email}")
    Boolean updateAccountByEmail(@Param("password") String password, @Param("email") String email);
}
