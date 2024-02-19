package com.Maxim.File_storage_API.service;


import com.Maxim.File_storage_API.entity.FileEntity;
import com.Maxim.File_storage_API.entity.Status;
import com.Maxim.File_storage_API.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    public Mono<FileEntity> saveFile (FileEntity file) {
        return fileRepository.save(file);
    }

    public Mono<FileEntity> getFileById(Integer fileId) {
        return fileRepository.findById(fileId);
    }

    public Flux<FileEntity> getAllFiles() {
        return fileRepository.findAll();
    }

//    public FileEntity updateFileById(File file) {
//
////TODO make it through, userRepository.existsById(id)
//// +  сделать сравнение полей юзера из бд, и полей из json
//        return null;
//    }

    public Mono<FileEntity> deleteFileById(Integer id) {
        return fileRepository.findById(id)
                .flatMap(fileEntity -> {
                    fileEntity.setStatus(Status.DELETED);
                    return fileRepository.save(fileEntity);
                });
    }
//public Mono<FileEntity> deleteFileById(Integer id) {
//    return fileRepository.updateFileStatus(id,Status.DELETED);
//}
}
