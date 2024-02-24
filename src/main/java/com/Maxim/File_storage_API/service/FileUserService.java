package com.Maxim.File_storage_API.service;

import com.Maxim.File_storage_API.entity.*;
import com.Maxim.File_storage_API.repository.EventRepository;
import com.Maxim.File_storage_API.repository.file_storage.S3RepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.GrantedAuthority;


import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Collection;

@Service
public class FileUserService {

    public FileUserService(FileService fileService, EventRepository eventRepository) {
        this.fileService = fileService;
        this.eventRepository = eventRepository;
    }

    @Autowired
    private FileService fileService;

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;


    public Flux<FileEntity> getAllFilesForRole(Collection<? extends GrantedAuthority> authorities, Integer userId) {
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(String.valueOf(Role.USER))) {
                return fileService.getFilesByUserId(userId);
            } else if (authority.getAuthority().equals(String.valueOf(Role.ADMIN)) | authority.getAuthority().equals(String.valueOf(Role.MODERATOR))) {
                return fileService.getAllFiles();
            }
        }
        return null;
    }

    public Mono<FileEntity> getFileByIDForRole(Collection<? extends GrantedAuthority> authorities, Integer userId, Integer fileId) {
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(String.valueOf(Role.ADMIN)) || authority.getAuthority().equals(String.valueOf(Role.MODERATOR))) {
                return fileService.getFileById(fileId);
            } else if (authority.getAuthority().equals(String.valueOf(Role.USER))) {
                return eventRepository.findFileByUserId(userId, fileId)
                        .flatMap(eventEntity -> {
                            if (eventEntity != null) {
                                return fileService.getFileById(fileId);
                            } else {
                                return Mono.empty();
                            }
                        });
            }
        }
        return Mono.empty();
    }

    public Mono<FileEntity> saveFile(Mono<FilePart> file, Integer userId) {
        String bucket = "files-strorage-repository";
        S3RepositoryImpl s3 = new S3RepositoryImpl();
//        TODO add events

        return file.flatMap(f -> {
            return s3.uploadFile(Mono.just(f), bucket, f.filename())
                    .map(filePath -> {
                        FileEntity fileEntity = new FileEntity();
                        fileEntity.setFilePath(filePath);
                        fileEntity.setName(f.filename());
                        fileEntity.setCreateAt(String.valueOf(LocalDate.now()));
                        fileEntity.setUpdatedAt(String.valueOf(LocalDate.now()));
                        fileEntity.setStatus(Status.ACTIVE);


                        UserEntity user = new UserEntity();
                        user.setId(userId);

                        EventEntity eventEntity = new EventEntity();
                        eventEntity.setUser(user);
                        eventEntity.setFile(fileEntity);
                        eventEntity.setStatus(Status.ACTIVE);

//                        eventService.save(eventEntity);
                        return fileEntity;
                    })
                    .flatMap(fileService::saveFile);
        });


    }
}
