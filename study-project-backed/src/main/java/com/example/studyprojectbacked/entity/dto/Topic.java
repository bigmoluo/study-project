package com.example.studyprojectbacked.entity.dto;

import lombok.Data;

import java.util.Date;

@Data
public class Topic {
	Integer id;
	String title;
	String content;
	Integer uid;
	Integer type;
	Date time;
	String username;
	String avatar;
}
