package com.example.studyprojectbacked.mapper;

import com.example.studyprojectbacked.entity.dto.Account;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AccountMapper {
    @Select("select * from db_account where username = #{text} or email = #{text}")
    Account getAccountByUsernameOrEmail(String text);

    @Insert("insert into db_account(email,username,password,role,register_time) " +
            "values (#{email},#{username},#{password},#{role},#{register_time})")
    Boolean saveAccount(Account account);

    @Update("update db_account set password = #{password} where email = #{email}")
    Boolean updateAccountByEmail(@Param("password") String password, @Param("email") String email);

    @Select("select * from db_account where id = #{id}")
    Account getAccountById(int id);
    @Update("update db_account set username = #{username} where id = #{id}")
    void updateAccountUsernameById(@Param("id") int id, @Param("username") String username);

    @Update("update db_account set email = #{email} where id = #{id}")
    void updateAccountEmailById(@Param("id")int id, @Param("email") String email);

    @Select("select password from db_account where id = #{id}")
    String getAccountPasswordById(int id);
    @Update("update db_account set password = #{password} where id = #{id}")
    boolean updateAccountPasswordById(@Param("id") int id,@Param("password") String password);
}
