package com.example.studyprojectbacked.mapper;

import com.example.studyprojectbacked.entity.dto.TopicType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TopicTypeMapper {
	@Select("select * from db_topic_type")
	List<TopicType> getTopicTypeList();
}
