package com.Maxim.File_storage_API.service;


import com.Maxim.File_storage_API.entity.EventEntity;
import com.Maxim.File_storage_API.entity.Status;
import com.Maxim.File_storage_API.repository.EventRepository;
import com.Maxim.File_storage_API.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.r2dbc.core.DatabaseClient;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private  R2dbcEntityTemplate template;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private DatabaseClient databaseClient;

    public EventService() {
    }

    public Mono<EventEntity> getEventById(Integer eventId) {
        return databaseClient.sql("SELECT file_id FROM rest.events where id = :eventId")
                .bind("eventId", eventId)
                .map(row -> row.get("file_id", Integer.class))
                .one()
                .flatMap(fileId -> fileRepository.findById(fileId)
                        .flatMap(file -> eventRepository.findById(eventId)
                                .map(event -> {
                                    event.setFile(file);
                                    return event;
                                })
                        )
                );
    }
    public Flux<EventEntity> getAllEvents() {
        return eventRepository.findAll().flatMap(event -> getEventById(event.getId()));
    }

    public Mono<EventEntity> save(EventEntity p) {
        return this.template.insert(EventEntity.class)
                .using(p)
                .map(post -> post);
    }

//    public Mono<EventEntity> saveEvents(EventEntity event) {
//
//
//        return databaseClient.sql("insert into events (user_id, file_id,status) values(:userId, :fileId, :status) ")
//                .bind("userId",event.getUser().getId())
//                .bind("fileId",event.getFile().getId())
//                .bind("status",event.getStatus())
//                .fetch();}

    public Mono<EventEntity> saveEvent(EventEntity event, Integer userId) {
        return databaseClient.sql("INSERT INTO events (user_id, file_id, status) VALUES (:userId, :fileId, :status)")
                .bind("userId", userId)
                .bind("fileId", event.getFile().getId())
                .bind("status", event.getStatus())
                .fetch()
                .rowsUpdated()
                .flatMap(rowsUpdated -> databaseClient.sql("SELECT last_insert_id() as id").map((row, rowMetadata) -> row.get("id", Integer.class)).one())
                .doOnNext(eventId -> event.setId(eventId))
                .thenReturn(event);
    }


    public Mono<EventEntity> updateEventById(EventEntity event) {
//        TODO когда вынесу в сервис в нем сделать проверку есть ли такой event_id
        return eventRepository.updateEventByIdAllColumns(event.getId(),event.getUser().getId(),event.getFile().getId(),event.getStatus())
                .then(getEventById(event.getId()));
    }

    public Mono<EventEntity> deleteEventById(Integer id) {
        return eventRepository.updateEventStatus(id, Status.DELETED);

    }






}
