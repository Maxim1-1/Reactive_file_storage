package com.Maxim.File_storage_API.service;


import com.Maxim.File_storage_API.entity.EventEntity;
import com.Maxim.File_storage_API.entity.Status;
import com.Maxim.File_storage_API.repository.EventRepository;
import com.Maxim.File_storage_API.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private FileRepository fileRepository;

//

    @Autowired
    private DatabaseClient databaseClient;
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

    @Transactional
    public Mono<EventEntity> saveEvents(EventEntity event) {
        return eventRepository.insertEvent(event.getUser().getId(), event.getFile().getId(), event.getStatus())
                .flatMap(savedEvent -> {
                    EventEntity eventWithId = new EventEntity();
                    eventWithId.setId(savedEvent.getId());
                    eventWithId.setUser(event.getUser());
                    eventWithId.setFile(event.getFile());
                    eventWithId.setStatus(event.getStatus());
                    return Mono.just(eventWithId);
                });
    }

    @Transactional
    public Mono<EventEntity> updateEventById(EventEntity event) {
        return eventRepository.updateEventByIdAllColumns(event.getId(),event.getUser().getId(),event.getFile().getId(),event.getStatus());
    }

    @Transactional
    public Mono<EventEntity> deleteEventById(Integer id) {
        return eventRepository.updateEventStatus(id, Status.DELETED);

    }






}
