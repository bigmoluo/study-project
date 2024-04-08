package com.example.studyprojectbacked.controller;

import com.example.studyprojectbacked.entity.RestBeen;
import com.example.studyprojectbacked.service.ImageService;
import com.example.studyprojectbacked.util.Const;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@Slf4j
@RestController
@RequestMapping("/api/image")
public class ImageController {
    @Resource
    ImageService imageService;
    @PostMapping("/avatar")
    public RestBeen<String> avatar(@RequestParam("file")MultipartFile file,
                                   @RequestAttribute(Const.ATTR_USER_ID) int id) throws IOException {
        if (file.getSize() > 1024 * 1024)
            return RestBeen.failure(400, "头像图片不能大于1024KB");
        log.info("正在进行头像上传...");
        String url = imageService.uploadAvatar(file, id);
        if (url != null){
            log.info("头像上次成功，大小：" + file.getSize());
            return RestBeen.success(url);
        } else {
            return RestBeen.failure(400, "头像上传失败，请联系管理员！");
        }
    }
}
