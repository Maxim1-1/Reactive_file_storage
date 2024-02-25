package com.Maxim.File_storage_API.repository;


import com.Maxim.File_storage_API.entity.FileEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface FileRepository extends ReactiveCrudRepository<FileEntity, Integer> {
    Flux<FileEntity> findAllById(Integer fileID);
}
