package com.Maxim.File_storage_API.controllers;

import com.Maxim.File_storage_API.dto.FileDTO;
import com.Maxim.File_storage_API.entity.EventEntity;
import com.Maxim.File_storage_API.entity.FileEntity;
import com.Maxim.File_storage_API.entity.Status;
import com.Maxim.File_storage_API.entity.UserEntity;
import com.Maxim.File_storage_API.mapper.FileMapper;
import com.Maxim.File_storage_API.repository.file_storage.S3RepositoryImpl;
import com.Maxim.File_storage_API.security.CustomPrincipal;
import com.Maxim.File_storage_API.service.FileService;
import com.Maxim.File_storage_API.service.FileUserService;
import com.Maxim.File_storage_API.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.stream.Stream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
public class FileRestControllerV1 {


    public FileRestControllerV1(FileMapper fileMapper, FileService fileService, FileUserService fileUserService) {
        this.fileMapper = fileMapper;
        this.fileService = fileService;
        this.fileUserService = fileUserService;
    }

    private FileMapper fileMapper;

    private final FileService fileService;

    private final FileUserService fileUserService;

    @GetMapping("/{id}")
    public Mono<FileDTO> getFileById(@PathVariable Integer id) {
        return fileService.getFileById(id).map(fileMapper::map);
    }

    @GetMapping("")
    public Flux<FileDTO> getAllFiles(Authentication authentication) {
        CustomPrincipal userDetails = (CustomPrincipal) authentication.getPrincipal();
        Integer userId = userDetails.getId();
        return fileUserService.getFilesForRole(authentication.getAuthorities(),userId).map(fileMapper::map);
    }


    @RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<FileEntity> saveFile(@RequestPart("file") Mono<FilePart> file) {

        String bucket = "files-strorage-repository";
        S3RepositoryImpl s3 = new  S3RepositoryImpl();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getAuthorities());

        return file.flatMap(f -> {
            return s3.uploadFile(Mono.just(f), bucket, f.filename())
                    .map(filePath -> {
                        FileEntity fileEntity = new FileEntity();
                        fileEntity.setFilePath(filePath);
                        fileEntity.setName(f.filename());
                        fileEntity.setCreateAt(String.valueOf(LocalDate.now()));
                        fileEntity.setUpdatedAt(String.valueOf(LocalDate.now()));
                        fileEntity.setStatus(Status.ACTIVE);
                        return fileEntity;
                    })
                    .flatMap(fileService::saveFile);
        });
    }






















    @PutMapping("/{id}")
    public Mono<FileEntity> updateFileById(@PathVariable Integer id,@RequestBody UserEntity user) {
        FileEntity file1 = new FileEntity();
        file1.setId(id);
        file1.setUpdatedAt("");
        file1.setName("fgggggggggggggg");

        return fileService.updateFileById(file1);
    }

    @DeleteMapping("/{id}")
    public Mono<FileEntity> deleteFileById(@PathVariable Integer id) {
        return fileService.deleteFileById(id);
    }

}
