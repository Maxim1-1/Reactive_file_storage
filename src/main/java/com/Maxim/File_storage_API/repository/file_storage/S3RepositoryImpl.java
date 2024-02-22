package com.Maxim.File_storage_API.repository.file_storage;

import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;
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


    String ACCESS_KEY = "";
    String SECRET_KEY = "";



    private S3Client getClient() {

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
//    public String uploadF2ile(Mono<FilePart> filePartMono, String bucketName, String fileName) {
//
//        S3Client s3 = getClient();
//        try {
////            String theTags = "name="+fileName+"&description="+description;
//            PutObjectRequest putOb = PutObjectRequest.builder()
//                    .bucket(bucketName)
//                    .key(fileName)
////                    .tagging(theTags)
//                    .build();
//
//            byte[] fileBytest = Mono.just(convertFilePartToByteArray(filePartMono));
//
//            s3.putObject(putOb, RequestBody.fromBytes(fileBytest));
//
//            return bucketName+"/"+fileName;
//
//        } catch (S3Exception e) {
//            System.err.println(e.awsErrorDetails().errorMessage());
//            System.exit(1);
//        }
//
//        return bucketName;
//    }


    public Mono<String> uploadFile(Mono<FilePart> filePartMono, String bucketName, String fileName) {
        S3Client s3 = getClient();

        return convertFilePartToByteArray(filePartMono)
                .flatMap(fileBytes -> {
                    PutObjectRequest putOb = PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .build();

                    return Mono.fromCallable(() -> {
                        s3.putObject(putOb, RequestBody.fromBytes(fileBytes));
                        return bucketName + "/" + fileName;
                    });
                })
                .onErrorMap(S3Exception.class, e -> {
                    System.err.println(e.awsErrorDetails().errorMessage());
                    return e;
                });
    }



    public Mono<byte[]> convertFilePartToByteArray(Mono<FilePart> filePartMono) {
        return filePartMono
                .flatMap(filePart -> DataBufferUtils.join(filePart.content())
                        .map(dataBuffer -> {
                            byte[] bytes = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(bytes);
                            DataBufferUtils.release(dataBuffer);
                            return bytes;
                        }));
    }

}

