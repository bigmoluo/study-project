package com.example.studyprojectbacked.config;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Slf4j
@Configuration
public class MinioConfiguration {
    @Value("${spring.minio.endpoint}")
    private String endpoint;
    @Value("${spring.minio.username}")
    private String useranem;
    @Value("${spring.minio.password}")
    private String password;
    @Bean
    public MinioClient minioClient(){
        log.info("Init minio client...");
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(useranem,password)
                .build();
    }
}
