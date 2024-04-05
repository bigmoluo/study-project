package com.example.studyprojectbacked.mapper;

import com.example.studyprojectbacked.entity.dto.AccountDetails;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AccountDetailsMapper {
    @Select("select * from db_account_details where id = #{id}")
    AccountDetails findAccountDetailsById(int id);

    @Insert("insert into db_account_details(id, gender, phone, qq, wx, desc) " +
            "values (#{id}, #{gender}, #{phone}, #{qq}, #{wx}, #{desc})")
    boolean saveAccountDetails(AccountDetails accountDetails);
    @Update("update db_account_details set gender = #{accountDetails.gender}, phone = #{accountDetails.phone}," +
            "qq = #{accountDetails.qq}, wx = #{accountDetails.wx}, desc = #{accountDetails.desc} where id = #{id}")
    boolean updateAccountDetails(@Param("id") int id, AccountDetails accountDetails);
}
