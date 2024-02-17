package com.Maxim.File_storage_API.repository.file_storage;

import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;






import java.net.URI;
import java.net.URISyntaxException;


public class S3RepositoryImpl implements GenericFileStorageRepository {

//    TODO вынести в проперти данные которе могут изменяться по типу url чтобы можно было работать с любым хранилищем
    private final String endpointUrl = "https://storage.yandexcloud.net";

    private S3Client getClient() {
        String ACCESS_KEY = "";
        String SECRET_KEY = "";


        String region = "ru-central1";

        AwsBasicCredentials credentials = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);

        S3Client s3 = null;
        try {
            s3 = S3Client.builder()
                    .region(Region.of(region))
                    .endpointOverride(new URI(endpointUrl))
                    .credentialsProvider(StaticCredentialsProvider.create(credentials))
                    .build();

            return s3;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

//    @Override
    public void uploadFile(byte[] bytes, String bucketName, String fileName, String description) {

        S3Client s3 = getClient();
        try {
//            String theTags = "name="+fileName+"&description="+description;
            PutObjectRequest putOb = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
//                    .tagging(theTags)
                    .build();

            s3.putObject(putOb, RequestBody.fromBytes(bytes));

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }

    }

}

