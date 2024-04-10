package com.example.studyprojectbacked.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class StoreImage {
	Integer uid;
	String name;
	Date time;
}
