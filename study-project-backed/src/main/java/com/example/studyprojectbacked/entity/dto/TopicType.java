package com.example.studyprojectbacked.entity.dto;

import com.example.studyprojectbacked.entity.BaseData;
import lombok.Data;

@Data
public class TopicType implements BaseData {
	Integer id;
	String name;
	String desc;
	String color;
}
