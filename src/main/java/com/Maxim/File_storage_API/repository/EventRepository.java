package com.Maxim.File_storage_API.repository;

import com.Maxim.File_storage_API.entity.EventEntity;
import com.Maxim.File_storage_API.entity.Status;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface EventRepository extends ReactiveCrudRepository<EventEntity, Integer> {

    @Query("SELECT id,status FROM rest.events where user_id=:id;")
    Flux<EventEntity> findAllIdRelatedEventsByUserId(Integer id);

    @Modifying
    @Query("UPDATE events SET user_id = :user_id, file_id= :file_id, status = :status WHERE id = :eventId;")
//    TODO харкод таблицы
    Mono<EventEntity> updateEventByIdAllColumns(Integer eventId, Integer user_id, Integer file_id, Status status);

    @Query("UPDATE events SET  status = :status WHERE id = :eventId;")
//    TODO харкод таблицы
    Mono<EventEntity> updateEventStatus(Integer eventId,Status status);

}
