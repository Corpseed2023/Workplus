package com.example.desktime.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSS3ClientConfig {

    @Autowired
    private AWSCredentialsConfig awsCredentialsConfig;

    @Bean
    public AmazonS3 initS3Client(){
        AWSCredentials credentials = new BasicAWSCredentials(
                awsCredentialsConfig.getAccessKey(),
                awsCredentialsConfig.getSecretKey()
        );
        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.AP_SOUTH_1)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }
}
