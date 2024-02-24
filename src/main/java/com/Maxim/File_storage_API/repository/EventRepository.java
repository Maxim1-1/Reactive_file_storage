package com.Maxim.File_storage_API.repository;

import com.Maxim.File_storage_API.entity.EventEntity;


import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface EventRepository extends R2dbcRepository<EventEntity, Integer> {

    Mono<EventEntity> findByUserIdAndFileId(Integer userId, Integer fileId);


    Flux<EventEntity> findAllByUserId(Integer userId);
    Mono<EventEntity> findEventByUserId(Integer userId);
}
