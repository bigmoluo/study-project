package com.example.studyprojectbacked.entity.vo.request;

import lombok.Data;

@Data
public class AddCommentVO {
	int tid;
	String content;
	int quote;
}
