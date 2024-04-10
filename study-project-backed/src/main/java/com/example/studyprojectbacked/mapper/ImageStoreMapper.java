package com.example.studyprojectbacked.mapper;

import com.example.studyprojectbacked.entity.dto.StoreImage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ImageStoreMapper {
	@Insert("insert into db_image_store(uid, name, time) values (#{uid}, #{name}, #{time})")
	boolean saveStoreImage(StoreImage image);
}
