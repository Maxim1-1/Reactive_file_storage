package com.Maxim.File_storage_API.service;


import com.Maxim.File_storage_API.entity.EventEntity;
import com.Maxim.File_storage_API.entity.Status;
import com.Maxim.File_storage_API.exceptions.service_exceptions.EventNotExistException;
import com.Maxim.File_storage_API.exceptions.service_exceptions.FileNotExistException;
import com.Maxim.File_storage_API.exceptions.service_exceptions.UserAndFilesNotExistException;
import com.Maxim.File_storage_API.exceptions.service_exceptions.UserNotExistException;
import com.Maxim.File_storage_API.repository.EventRepository;
import com.Maxim.File_storage_API.repository.FileRepository;
import com.Maxim.File_storage_API.repository.UserRepository;
import io.r2dbc.spi.R2dbcDataIntegrityViolationException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class EventService {
    public EventService(EventRepository eventRepository, FileRepository fileRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }

    private final EventRepository eventRepository;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;


    public Mono<EventEntity> getEventById(Integer eventId) {
        return eventRepository.findById(eventId).switchIfEmpty(Mono.error(new EventNotExistException(eventId)));
    }

    public Flux<EventEntity> getAllEvents() {
        return eventRepository.findAll();
    }

    public Mono<List<EventEntity>> findAllByUserID (Integer id) {
        return eventRepository.findAllByUserId(id).collectList();
    }

    public Mono<EventEntity> saveEvents(EventEntity event) {
        return Mono.zip(
                        userRepository.existsById(event.getUserId()),
                        fileRepository.existsById(event.getFileId())
                )
                .flatMap(tuple -> {
                    boolean userExists = tuple.getT1();
                    boolean fileExists = tuple.getT2();
                    if (!userExists && !fileExists) {
                        return Mono.error(new UserAndFilesNotExistException(event.getUserId(), event.getFileId()));
                    } else if (!userExists) {
                        return Mono.error(new UserNotExistException(event.getUserId()));
                    } else if (!fileExists) {
                        return Mono.error(new FileNotExistException(event.getFileId()));
                    } else {
                        event.setStatus(Status.ACTIVE);
                        return eventRepository.save(event);
                    }
                })
                .switchIfEmpty(Mono.error(new Exception("Bad Request")));
    }


    public Mono<EventEntity> updateEventById(EventEntity event, Integer eventId) {
        event.setId(eventId);
        return eventRepository.findById(eventId)
                .flatMap(existEvent -> {
                    if (event.getFileId() != null && event.getFileId() != (existEvent.getFileId())) {
                        existEvent.setFileId(event.getFileId());
                    }
                    if (event.getUserId() != null && event.getUserId() != (existEvent.getUserId())) {
                        existEvent.setUserId(event.getUserId());
                    }
                    if (event.getStatus() != null && event.getStatus() != (existEvent.getStatus())) {
                        existEvent.setStatus(event.getStatus());
                    }
                    return eventRepository.save(existEvent);

                }).switchIfEmpty(Mono.error(new EventNotExistException(eventId)));
    }


    public Mono<EventEntity> deleteEventById(Integer eventId) {
        return eventRepository.findById(eventId).flatMap(fileEntity -> {
            fileEntity.setStatus(Status.DELETED);
            return eventRepository.save(fileEntity);
        }).switchIfEmpty(Mono.error(new EventNotExistException(eventId)));
    }


}
