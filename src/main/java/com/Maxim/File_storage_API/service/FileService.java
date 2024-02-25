package com.Maxim.File_storage_API.service;


import com.Maxim.File_storage_API.entity.FileEntity;
import com.Maxim.File_storage_API.entity.Status;
import com.Maxim.File_storage_API.exceptions.service_exceptions.FileNotExistException;
import com.Maxim.File_storage_API.repository.EventRepository;
import com.Maxim.File_storage_API.repository.FileRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
public class FileService {


    public FileService(FileRepository fileRepository, EventRepository eventRepository) {
        this.fileRepository = fileRepository;
        this.eventRepository = eventRepository;
    }

    private FileRepository fileRepository;

    private EventRepository eventRepository;

    public Flux<FileEntity> getFilesByUserId(Integer userId) {
        return eventRepository.findAllByUserId(userId)
                .flatMap(eventEntity -> fileRepository.findById(eventEntity.getFileId()));
    }


    public Mono<FileEntity> saveFile(FileEntity file) {
        return fileRepository.save(file);
    }

    public Mono<FileEntity> getFileById(Integer fileId) {
        return fileRepository.existsById(fileId)
                .flatMap(exists -> {
                    if (exists) {
                        return fileRepository.findById(fileId);
                    } else {
                        return Mono.error(new FileNotExistException(fileId));
                    }
                });
    }

    public Flux<FileEntity> getAllFiles() {
        return fileRepository.findAll();
    }


    public Mono<FileEntity> updateFileById(FileEntity file, Integer fileId) {
        return fileRepository.existsById(fileId)
                .flatMap(exists -> {
                    if (exists) {
                        file.setId(fileId);
                        return fileRepository.findById(fileId)
                                .map(fileRepository -> {
                                    if (file.getFilePath() != null & file.getFilePath().equalsIgnoreCase(fileRepository.getFilePath())) {
                                        fileRepository.setFilePath(file.getFilePath());
                                    }
                                    if (file.getUpdatedAt() != null & file.getUpdatedAt().equalsIgnoreCase(fileRepository.getUpdatedAt())) {
                                        fileRepository.setUpdatedAt(file.getUpdatedAt());
                                    }
                                    if (file.getCreateAt() != null & file.getCreateAt().equalsIgnoreCase(fileRepository.getCreateAt())) {
                                        fileRepository.setCreateAt(file.getCreateAt());
                                    }
                                    if (file.getName() != null & file.getName().equalsIgnoreCase(fileRepository.getName())) {
                                        fileRepository.setName(file.getName());
                                    }
                                    if (file.getStatus() != null & file.getStatus() != (fileRepository.getStatus())) {
                                        fileRepository.setStatus(file.getStatus());
                                    }
                                    fileRepository.setUpdatedAt(String.valueOf(LocalDate.now()));
                                    return fileRepository;
                                })
                                .flatMap(updatedEvent -> fileRepository.save(updatedEvent));
                    } else {
                        return Mono.error(new FileNotExistException(file.getId()));
                    }
                });
    }

    public Mono<FileEntity> deleteFileById(Integer id) {
        return fileRepository.existsById(id)
                .flatMap(exists -> {
                    if (exists) {
                        return fileRepository.findById(id).flatMap(file -> {
                            file.setStatus(Status.DELETED);
                            return fileRepository.save(file);
                        });
                    } else {
                        return Mono.error(new FileNotExistException(id));
                    }
                });
    }

}
