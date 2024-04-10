package com.example.studyprojectbacked.controller;

import com.example.studyprojectbacked.entity.RestBeen;
import com.example.studyprojectbacked.service.ImageService;
import io.minio.errors.ErrorResponseException;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
public class ObjectController {
    @Resource
    ImageService imageService;
    @GetMapping("/images/**")
    public void imageFetch(HttpServletRequest request,HttpServletResponse response) throws Exception {
        response.setHeader("Content-type", "image/jpg");
        this.fetchImage(request,response);
    }

    private void fetchImage(HttpServletRequest request,HttpServletResponse response) throws Exception{
        String imagePath = request.getServletPath().substring(7);
        log.info(imagePath);
        ServletOutputStream stream = response.getOutputStream();
        if (imagePath.length() <= 13) {
            response.setStatus(404);
            stream.println(RestBeen.failure(404, "Not Found").toString());
        } else {
            try {
                imageService.fetchImageFromMinio(stream, imagePath);
                response.setHeader("Cache-Control", "max-age=2592000");

            } catch (ErrorResponseException e){
                if (e.response().code() == 404) {
                    response.setStatus(404);
                    stream.println(RestBeen.failure(404, "Not Found").toString());
                } else {
                    log.error("从Minio获取图片出现异常：" + e.getMessage(), e);
                }
            }
        }
    }
}
