package com.example.studyprojectbacked.service.impl;

import com.example.studyprojectbacked.mapper.AccountMapper;
import com.example.studyprojectbacked.service.ImageService;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
@Slf4j
@Service
public class ImageServiceImpl implements ImageService {
    @Resource
    MinioClient minioClient;
    @Resource
    AccountMapper accountMapper;
    @Override
    public String uploadAvatar(MultipartFile file, int id) throws IOException {
        String imageName = UUID.randomUUID().toString().replace("-","");
        imageName = "/avatar/" + imageName;
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket("study")
                .object(imageName)
                .stream(file.getInputStream(),file.getSize(),-1)
                .build();
        try {
            minioClient.putObject(args);
            if (accountMapper.updateAccountAvatarById(id,imageName)){
                return imageName;
            } else {
                return null;
            }
        } catch (Exception e){
            log.info("图片上传出现问题：" + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void fetchImageFromMinio(OutputStream stream, String image) throws Exception{
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket("study")
                .object(image)
                .build();
        GetObjectResponse response = minioClient.getObject(args);
        IOUtils.copy(response, stream);
    }
}
