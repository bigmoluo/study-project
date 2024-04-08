package com.example.studyprojectbacked.mapper;

import com.example.studyprojectbacked.entity.dto.AccountPrivacy;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AccountPrivacyMapper {
    @Select("select * from db_account_privacy where id = #{id}")
    AccountPrivacy getAccountPrivacyById(int id);
    @Insert("insert into db_account_privacy(id,phone,email,qq,wx,gender) " +
            "values(#{id},#{phone},#{email},#{qq},#{wx},#{gender})")
    void saveAccountPrivacy(AccountPrivacy privacy);
    @Update("update db_account_privacy set phone = #{privacy.phone}, email = #{privacy.email}, qq = #{privacy.qq}," +
            "wx = #{privacy.wx}, gender = #{privacy.gender} where id = #{id}")
    void updateAccountPrivacyById(@Param("id") int id, AccountPrivacy privacy);
}
