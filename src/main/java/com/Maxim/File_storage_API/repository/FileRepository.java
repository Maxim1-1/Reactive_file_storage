package com.Maxim.File_storage_API.repository;

import com.Maxim.File_storage_API.entity.EventEntity;
import com.Maxim.File_storage_API.entity.FileEntity;
import com.Maxim.File_storage_API.entity.Status;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface FileRepository extends ReactiveCrudRepository<FileEntity, Integer> {

//    @Query("SELECT * FROM rest.files where id=:id;")
//        //    TODO харкод таблицы
//    Mono<FileEntity> findByFileId(Integer id);

    @Query("UPDATE files SET  status = :status WHERE id = :fileId;")
//    TODO харкод таблицы
    Mono<FileEntity> updateFileStatus(Integer fileId, Status status);


}
