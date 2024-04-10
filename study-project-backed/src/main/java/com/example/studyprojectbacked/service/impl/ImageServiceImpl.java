package com.example.studyprojectbacked.service.impl;

import com.example.studyprojectbacked.entity.dto.StoreImage;
import com.example.studyprojectbacked.mapper.AccountMapper;
import com.example.studyprojectbacked.mapper.ImageStoreMapper;
import com.example.studyprojectbacked.service.ImageService;
import com.example.studyprojectbacked.util.Const;
import com.example.studyprojectbacked.util.FlowUtil;
import io.minio.*;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
@Slf4j
@Service
public class ImageServiceImpl implements ImageService {
    @Resource
    MinioClient minioClient;
    @Resource
    AccountMapper accountMapper;
    @Resource
    FlowUtil flowUtil;
    @Resource
    ImageStoreMapper imageStoreMapper;
    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
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
            String avatar = accountMapper.getAccountAvatarById(id);
            this.deleteOldAvatar(avatar);
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

    @Override
    public String uploadImage(MultipartFile file, int id) throws IOException {
        String key = Const.FORUM_IMAGE_COUNTER + id;
        if (!flowUtil.limitPeriodCounterCheck(key,20,3600))
            return null;
        String imageName = UUID.randomUUID().toString().replace("-","");
        Date date = new Date();
        imageName = "/cache/" + format.format(date) + "/" +imageName;
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket("study")
                .object(imageName)
                .stream(file.getInputStream(),file.getSize(),-1)
                .build();
        try {
            minioClient.putObject(args);
            if (imageStoreMapper.saveStoreImage(new StoreImage(id,imageName,date))){
                return imageName;
            } else {
                return null;
            }
        } catch (Exception e){
            log.info("图片上传出现问题：" + e.getMessage(), e);
            return null;
        }
    }

    private void deleteOldAvatar(String avatar) throws Exception{
        if (avatar == null || avatar.isEmpty()) return;
        RemoveObjectArgs remove = RemoveObjectArgs.builder()
                .bucket("study")
                .object(avatar)
                .build();
        minioClient.removeObject(remove);
    }
}
