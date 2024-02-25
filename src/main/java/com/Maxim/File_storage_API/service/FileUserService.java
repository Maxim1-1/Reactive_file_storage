package com.Maxim.File_storage_API.service;

import com.Maxim.File_storage_API.dto.HistoryDTO;
import com.Maxim.File_storage_API.entity.*;
import com.Maxim.File_storage_API.repository.EventRepository;
import com.Maxim.File_storage_API.repository.file_storage.S3RepositoryImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.GrantedAuthority;


import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Collection;

@Service
public class FileUserService {

    public FileUserService(FileService fileService, EventService eventService, EventRepository eventRepository, UserService userService, S3RepositoryImpl s3) {
        this.fileService = fileService;
        this.eventService = eventService;
        this.eventRepository = eventRepository;
        this.userService = userService;
        this.s3 = s3;
    }

    private FileService fileService;

    private EventService eventService;

    private UserService userService;

    private EventRepository eventRepository;

    private S3RepositoryImpl s3;

    @Value("${s3.bucket}")
    private String bucket;

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

    public Mono<FileEntity> getFileByIdForRole(Collection<? extends GrantedAuthority> authorities, Integer userId, Integer fileId) {
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(String.valueOf(Role.ADMIN)) || authority.getAuthority().equals(String.valueOf(Role.MODERATOR))) {
                return fileService.getFileById(fileId);
            } else if (authority.getAuthority().equals(String.valueOf(Role.USER))) {
                return eventRepository.findByUserIdAndFileId(userId, fileId)
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
        return file.flatMap(f -> {
            return s3.uploadFile(Mono.just(f), bucket, f.filename())
                    .flatMap(filePath -> {
                        FileEntity fileEntity = new FileEntity();
                        fileEntity.setFilePath(filePath);
                        fileEntity.setName(f.filename());
                        fileEntity.setCreateAt(String.valueOf(LocalDate.now()));
                        fileEntity.setUpdatedAt(String.valueOf(LocalDate.now()));
                        fileEntity.setStatus(Status.ACTIVE);

                        return fileService.saveFile(fileEntity)
                                .flatMap(savedFileEntity -> {
                                    EventEntity eventEntity = new EventEntity();
                                    eventEntity.setFileId(savedFileEntity.getId());
                                    eventEntity.setUserId(userId);

                                    return eventService.saveEvents(eventEntity)
                                            .thenReturn(savedFileEntity);
                                });
                    });
        });
    }


    public Flux<HistoryDTO> getHistory(Collection<? extends GrantedAuthority> authorities, Integer userId) {
        return Flux.fromIterable(authorities)
                .flatMap(authority -> {
                    if (authority.getAuthority().equals(String.valueOf(Role.USER))) {
                        return fileService.getFilesByUserId(userId)
                                .map(file -> {
                                    HistoryDTO historyDTO = new HistoryDTO();
                                    historyDTO.setCreateAt(file.getCreateAt());
                                    historyDTO.setFileId(file.getId());
                                    historyDTO.setFileName(file.getName());
                                    return historyDTO;
                                });
                    } else if (authority.getAuthority().equals(String.valueOf(Role.ADMIN)) || authority.getAuthority().equals(String.valueOf(Role.MODERATOR))) {
                        return userService.findAllUsers()
                                .flatMap(user -> fileService.getFilesByUserId(user.getId())
                                        .map(file -> {
                                            HistoryDTO historyDTO = new HistoryDTO();
                                            historyDTO.setAuthor(user.getName());
                                            historyDTO.setCreateAt(file.getCreateAt());
                                            historyDTO.setFileId(file.getId());
                                            historyDTO.setFileName(file.getName());
                                            return historyDTO;
                                        }));
                    } else {
                        return Flux.empty();
                    }
                });
    }

}



