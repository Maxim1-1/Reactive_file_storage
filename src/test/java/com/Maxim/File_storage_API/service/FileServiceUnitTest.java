package com.Maxim.File_storage_API.service;

import com.Maxim.File_storage_API.entity.FileEntity;
import com.Maxim.File_storage_API.entity.Status;
import com.Maxim.File_storage_API.exceptions.service_exceptions.FileNotExistException;
import com.Maxim.File_storage_API.repository.FileRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class FileServiceUnitTest {
    @Mock
    FileRepository fileRepository;

    @InjectMocks
    FileService fileService;

    @Test
    void findAllFilesTestSuccess() {
        FileEntity file = new FileEntity();
        file.setName("test");
        file.setStatus(Status.ACTIVE);

        FileEntity file2 = new FileEntity();
        file.setName("test");
        file.setStatus(Status.ACTIVE);

        when(fileRepository.findAll()).thenReturn(Flux.just(file, file2));

        StepVerifier
                .create(fileService.getAllFiles())
                .expectNext(file)
                .expectNext(file2)
                .verifyComplete();
    }

    @Test
    void findFileByIdTestSuccess() {
        Integer fileId = 1;
        FileEntity fileEntity = new FileEntity();
        fileEntity.setId(fileId);
        fileEntity.setName("test");
        fileEntity.setStatus(Status.ACTIVE);

        when(fileRepository.existsById(fileId)).thenReturn(Mono.just(true));
        when(fileRepository.findById(fileId)).thenReturn(Mono.just(fileEntity));

        Mono<FileEntity> result = fileService.getFileById(fileId);

        StepVerifier.create(result)
                .expectNext(fileEntity)
                .verifyComplete();
    }

    @Test
    public void testFindFileById_NonExistingFile() {
        Integer userId = 1;
        when(fileRepository.findById(userId)).thenReturn(Mono.empty());
        Mono<FileEntity> result = fileService.getFileById(userId);
        result.subscribe(
                userEntity -> {
                    assertEquals(null, userEntity);
                },
                error -> {
                    assertEquals("FileNotExistException", error.getClass().getSimpleName());
                    FileNotExistException userNotExistException = (FileNotExistException) error;
                    assertEquals(userId, userNotExistException.getId());
                }
        );
        Mockito.verify(fileRepository).findById(userId);
    }

    @Test
    void saveFileSuccess() {
        Integer userId = 1;
        FileEntity file = new FileEntity();
        file.setName("test");

        FileEntity savedUser = new FileEntity();
        savedUser.setId(userId);
        savedUser.setName(file.getName());

        when(fileRepository.save(file)).thenReturn(Mono.just(savedUser));

        Mono<FileEntity> result = fileService.saveFile(file);

        StepVerifier.create(fileService.saveFile(file))
                .assertNext(u -> {
                    assertEquals("test", u.getName());
                    assertEquals(userId, u.getId());
                })
                .verifyComplete();
    }


    @Test
    void updateFileByIdSuccess() {
        FileEntity file = new FileEntity();
        file.setFilePath("path_test");
        file.setUpdatedAt("test");
        file.setCreateAt("test");
        file.setId(1);
        file.setName("test");
        file.setStatus(Status.ACTIVE);

        FileEntity updatedFile = new FileEntity();
        updatedFile.setCreateAt("updated_create_data");
        updatedFile.setFilePath("updated_test_path");
        updatedFile.setUpdatedAt("updated_data");
        updatedFile.setId(1);
        updatedFile.setName("test_update");
        updatedFile.setStatus(Status.DELETED);

        when(fileRepository.existsById(1)).thenReturn(Mono.just(true));
        when(fileRepository.findById(1)).thenReturn(Mono.just(file));
        when(fileRepository.save(file)).thenReturn(Mono.just(updatedFile));

        Mono<FileEntity> resultMono = fileService.updateFileById(file, 1);

        StepVerifier.create(resultMono)
                .assertNext(result -> {
                    assertEquals(1, result.getId());
                    assertEquals("test_update", result.getName());
                    assertEquals("updated_create_data", result.getCreateAt());
                    assertEquals("updated_data", result.getUpdatedAt());
                    assertEquals("updated_test_path", result.getFilePath());
                    assertEquals("test_update", result.getName());
                    assertEquals(Status.DELETED, result.getStatus());
                })
                .verifyComplete();
    }

    @Test
    void updateFileById_NonExistingUser() {
        Integer fileId = 1;
        FileEntity file = new FileEntity();
        file.setId(1);
        file.setName("test");
        file.setStatus(Status.ACTIVE);

        when(fileRepository.findById(fileId)).thenReturn(Mono.empty());
        Mono<FileEntity> result = fileService.updateFileById(file,fileId);

        StepVerifier.create(result)
                .expectError(FileNotExistException.class)
                .verify();
    }

    @Test
    void deleteFileByIdSuccess() {
        Integer userId = 1;
        FileEntity file = new FileEntity();
        file.setId(userId);
        file.setName("test");
        file.setStatus(Status.ACTIVE);

        FileEntity deleteFile = new FileEntity();
        deleteFile.setId(userId);
        deleteFile.setName("test");
        deleteFile.setStatus(Status.DELETED);

        when(fileRepository.existsById(userId)).thenReturn(Mono.just(true));
        when(fileRepository.findById(userId)).thenReturn(Mono.just(file));
        when(fileRepository.save(file)).thenReturn(Mono.just(deleteFile));

        StepVerifier.create(fileService.deleteFileById(userId))
                .expectNextMatches(u -> Status.DELETED.equals(u.getStatus()))
                .verifyComplete();

    }
    @Test
    void deleteFileById_NonExistingUser() {
        Integer fileId = 1;
        when(fileRepository.findById(fileId)).thenReturn(Mono.empty());
        Mono<FileEntity> result = fileService.deleteFileById(fileId);

        StepVerifier.create(result)
                .expectError(FileNotExistException.class)
                .verify();
    }
}