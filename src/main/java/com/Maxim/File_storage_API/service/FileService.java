package com.Maxim.File_storage_API.service;


import com.Maxim.File_storage_API.entity.FileEntity;
import com.Maxim.File_storage_API.entity.Status;
import com.Maxim.File_storage_API.exceptions.service_exceptions.EventNotExistException;
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

    private final FileRepository fileRepository;

    private final EventRepository eventRepository;

    public Flux<FileEntity> getFilesByUserId(Integer userId) {
        return eventRepository.findAllByUserId(userId)
                .flatMap(eventEntity -> fileRepository.findById(eventEntity.getFileId()));
    }


    public Mono<FileEntity> saveFile(FileEntity file) {
        return fileRepository.save(file);
    }

    public Mono<FileEntity> getFileById(Integer fileId) {
        return fileRepository.findById(fileId).switchIfEmpty(Mono.error(new FileNotExistException(fileId)));
    }

    public Flux<FileEntity> getAllFiles() {
        return fileRepository.findAll();
    }


    public Mono<FileEntity> updateFileById(FileEntity file, Integer fileId) {
        file.setId(fileId);
        return fileRepository.findById(fileId)
                .flatMap(existFile -> {
                    if (file.getFilePath() != null & file.getFilePath().equalsIgnoreCase(existFile.getFilePath())) {
                        existFile.setFilePath(file.getFilePath());
                    }
                    if (file.getUpdatedAt() != null & file.getUpdatedAt().equalsIgnoreCase(existFile.getUpdatedAt())) {
                        existFile.setUpdatedAt(file.getUpdatedAt());
                    }
                    if (file.getCreateAt() != null & file.getCreateAt().equalsIgnoreCase(existFile.getCreateAt())) {
                        existFile.setCreateAt(file.getCreateAt());
                    }
                    if (file.getName() != null & file.getName().equalsIgnoreCase(existFile.getName())) {
                        existFile.setName(file.getName());
                    }
                    if (file.getStatus() != null & file.getStatus() != (existFile.getStatus())) {
                        existFile.setStatus(file.getStatus());
                    }
                    existFile.setUpdatedAt(String.valueOf(LocalDate.now()));
                    return fileRepository.save(existFile);
                })
                .switchIfEmpty(Mono.error(new FileNotExistException(fileId)));


    }

    public Mono<FileEntity> deleteFileById(Integer id) {
        return fileRepository.findById(id).flatMap(file -> {
            file.setStatus(Status.DELETED);
            return fileRepository.save(file);
        }).switchIfEmpty(Mono.error(new FileNotExistException(id)));

    }

}
