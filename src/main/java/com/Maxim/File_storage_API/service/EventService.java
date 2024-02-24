package com.Maxim.File_storage_API.service;


import com.Maxim.File_storage_API.entity.EventEntity;
import com.Maxim.File_storage_API.entity.FileEntity;
import com.Maxim.File_storage_API.entity.Status;
import com.Maxim.File_storage_API.repository.EventRepository;
import com.Maxim.File_storage_API.repository.FileRepository;
import com.Maxim.File_storage_API.repository.UserRepository;
import io.r2dbc.spi.R2dbcDataIntegrityViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
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
    private UserRepository userRepository;


    @Autowired
    private DatabaseClient databaseClient;

    public EventService() {
    }



    public Mono<EventEntity> getEventById(Integer eventId) {
        return eventRepository.findById(eventId);
    }

    public Flux<EventEntity> getAllEvents() {
        return eventRepository.findAll();
    }

    public Mono<EventEntity> saveEvents(EventEntity event)  {
        return eventRepository.save(event);
    }

    public Mono<EventEntity> updateEventById(EventEntity event, Integer eventId) {
        return eventRepository.existsById(eventId)
                .flatMap(exists -> {
                    if (exists) {
                        event.setId(eventId);
                        return eventRepository.findById(eventId)
                                .map(eventRepository -> {
                                    if (event.getFileId()!=null & event.getFileId()!=(eventRepository.getFileId())) {
                                        eventRepository.setFileId(event.getFileId());
                                    }
                                    if (event.getUserId()!=null & event.getUserId()!=(eventRepository.getUserId())) {
                                        eventRepository.setUserId(event.getUserId());
                                    }
                                    if (event.getStatus()!=null & event.getStatus()!=(eventRepository.getStatus())) {
                                        eventRepository.setStatus(event.getStatus());
                                    }
                                    return eventRepository;
                                })
                                .flatMap(updatedEvent -> eventRepository.save(updatedEvent));
                    } else {
                        return Mono.error(new Exception("Event does not exist"));
                    }
                });
    }


    public Mono<EventEntity> deleteEventById(Integer id) {
        return eventRepository.findById(id)
                .flatMap(fileEntity -> {
                    fileEntity.setStatus(Status.DELETED);
                    return eventRepository.save(fileEntity);
                });
    }







}
