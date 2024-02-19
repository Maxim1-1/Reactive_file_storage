package com.Maxim.File_storage_API.controllers;

import com.Maxim.File_storage_API.entity.EventEntity;
import com.Maxim.File_storage_API.entity.FileEntity;
import com.Maxim.File_storage_API.entity.Status;
import com.Maxim.File_storage_API.entity.UserEntity;
import com.Maxim.File_storage_API.service.FileService;
import com.Maxim.File_storage_API.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileRestControllerV1 {

    @Autowired
    private FileService fileService;

    @GetMapping("/{id}")
    public Mono<FileEntity> getUserById(@PathVariable Integer id) {
        return fileService.getFileById(id);
    }

    @GetMapping("")
    public Flux<FileEntity> getAllUsers() {
        return fileService.getAllFiles();
    }

    @PostMapping("")
    public Mono<FileEntity> saveUser(@RequestBody FileEntity file) {
        return fileService.saveFile(file);
    }

    @PutMapping("/{id}")
    public Mono<FileEntity> updateUserById(@PathVariable Integer id,@RequestBody UserEntity user) {
        FileEntity file1 = new FileEntity();
        file1.setId(id);
        file1.setUpdatedAt("");
        file1.setName("fgggggggggggggg");

        return fileService.updateFileById(file1);
    }

    @DeleteMapping("/{id}")
    public Mono<FileEntity> deleteUserById(@PathVariable Integer id) {
        return fileService.deleteFileById(id);
    }

}
