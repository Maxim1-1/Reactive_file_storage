package com.Maxim.File_storage_API.repository.file_storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import java.net.URI;
import java.net.URISyntaxException;

@Component
public class S3RepositoryImpl {

    public S3RepositoryImpl( ) {
    }

    @Value("${s3.urlStorage}")
    private  String endpointUrl;
    @Value("${s3.region}")
    private  String region;
    @Value("${s3.ACCESS_KEY}")
    private String ACCESS_KEY;
    @Value("${s3.SECRET_KEY}")
    private String SECRET_KEY;



    private S3Client getClient() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);
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



    private Mono<byte[]> convertFilePartToByteArray(Mono<FilePart> filePartMono) {
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

