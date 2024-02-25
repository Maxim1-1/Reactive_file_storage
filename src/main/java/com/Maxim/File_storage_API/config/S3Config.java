package com.Maxim.File_storage_API.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class S3Config {
    @Value("${s3.urlStorage}")
    private String endpointUrl;
    @Value("${s3.region}")
    private String region;
    @Value("${s3.ACCESS_KEY}")
    private String ACCESS_KEY;
    @Value("${s3.SECRET_KEY}")
    private String SECRET_KEY;
    @Bean
    public AwsBasicCredentials getCredentials() {
        return AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);
    }
    @Bean
    public S3Client getClient(AwsBasicCredentials credentials) {
        try {
            S3Client s3 = S3Client.builder()
                    .region(Region.of(region))
                    .endpointOverride(new URI(endpointUrl))
                    .credentialsProvider(StaticCredentialsProvider.create(credentials))
                    .build();
            return s3;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
