package com.Maxim.File_storage_API.repository;

import com.Maxim.File_storage_API.entity.EventEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface EventRepository extends ReactiveCrudRepository<EventEntity, Integer> {

    @Query("SELECT id,status FROM rest.events where user_id=:id;")
//    TODO харкод таблицы
    Flux<EventEntity> findAllByUserId(Integer id);



}
