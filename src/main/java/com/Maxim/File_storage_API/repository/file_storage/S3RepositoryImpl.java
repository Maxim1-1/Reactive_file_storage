package com.Maxim.File_storage_API.repository.file_storage;


import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


@Component
public class S3RepositoryImpl {
    private  S3AsyncClient s3Async;

    public S3RepositoryImpl(S3AsyncClient s3Async) {
        this.s3Async = s3Async;
    }

    public Mono<String> uploadFile(Mono<FilePart> filePartMono, String bucketName, String fileName) {
        return convertFilePartToByteArray(filePartMono)
                .flatMap(fileBytes -> {
                    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .build();
                    return Mono.fromCompletionStage(s3Async.putObject(putObjectRequest, AsyncRequestBody.fromBytes(fileBytes)))
                            .thenReturn(bucketName + "/" + fileName);
                })
                .doOnError(e -> {
                    System.err.println("Error uploading file to S3: " + e.getMessage());
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





