package com.example.studyprojectbacked.entity.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TopicComment {
	Integer id;
	Integer uid;
	Integer tid;
	String content;
	Date time;
	Integer quote;
}
