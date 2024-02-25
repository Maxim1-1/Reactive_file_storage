package com.Maxim.File_storage_API.repository.file_storage;


import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;


@Component
public class S3RepositoryImpl {
    public S3RepositoryImpl(S3Client s3) {
        this.s3 = s3;
    }

    private S3Client s3;

    public Mono<String> uploadFile(Mono<FilePart> filePartMono, String bucketName, String fileName) {
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

