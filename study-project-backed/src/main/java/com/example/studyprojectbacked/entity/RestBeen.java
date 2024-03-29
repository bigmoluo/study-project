package com.example.studyprojectbacked.entity;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;

public record RestBeen<T> (int code, T data, String message) {

    public static <T> RestBeen<T> success(T data){
        return new RestBeen<>(200,data,"请求成功");
    }

    public static <T> RestBeen<T> failure(int code, String message){
        return new RestBeen<>(code,null,message);
    }

    public static <T> RestBeen<T> failure(int code){
        return failure(code,"请求失败");
    }

    public String asJsonString(){
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls);
    }
}
