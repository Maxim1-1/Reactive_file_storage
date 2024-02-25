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

@Service
public class EventService {
    public EventService(EventRepository eventRepository, FileRepository fileRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }

    private EventRepository eventRepository;
    private FileRepository fileRepository;
    private UserRepository userRepository;



    public Mono<EventEntity> getEventById(Integer eventId) {
        return eventRepository.existsById(eventId)
                .flatMap(exists -> {
                    if (exists) {
                        return eventRepository.findById(eventId);
                    } else {
                        return Mono.error(new EventNotExistException(eventId));
                    }
                });
    }

    public Flux<EventEntity> getAllEvents() {
        return eventRepository.findAll();
    }

    public Mono<EventEntity> saveEvents(EventEntity event) throws R2dbcDataIntegrityViolationException {
        Mono<Boolean> userExists = userRepository.existsById(event.getUserId());
        Mono<Boolean> fileExists = fileRepository.existsById(event.getFileId());
        return Mono.zip(userExists, fileExists)
                .flatMap(tuple -> {
                    if (!tuple.getT1() && !tuple.getT2()) {
                        return Mono.error(new UserAndFilesNotExistException(event.getUserId(), event.getFileId()));
                    } else if (!tuple.getT1()) {
                        return Mono.error(new UserNotExistException(event.getUserId()));
                    } else if (!tuple.getT2()) {
                        return Mono.error(new FileNotExistException(event.getFileId()));
                    } else {
                        event.setStatus(Status.ACTIVE);
                        return eventRepository.save(event);
                    }
                })
                .switchIfEmpty(Mono.error(new Exception("Bad Request")));
    }

    public Mono<EventEntity> updateEventById(EventEntity event, Integer eventId) {
        return eventRepository.existsById(eventId)
                .flatMap(exists -> {
                    if (exists) {
                        event.setId(eventId);
                        return eventRepository.findById(eventId)
                                .map(eventRepository -> {
                                    if (event.getFileId() != null & event.getFileId() != (eventRepository.getFileId())) {
                                        eventRepository.setFileId(event.getFileId());
                                    }
                                    if (event.getUserId() != null & event.getUserId() != (eventRepository.getUserId())) {
                                        eventRepository.setUserId(event.getUserId());
                                    }
                                    if (event.getStatus() != null & event.getStatus() != (eventRepository.getStatus())) {
                                        eventRepository.setStatus(event.getStatus());
                                    }
                                    return eventRepository;
                                })
                                .flatMap(updatedEvent -> eventRepository.save(updatedEvent));
                    } else {
                        return Mono.error(new EventNotExistException(eventId));
                    }
                });
    }


    public Mono<EventEntity> deleteEventById(Integer eventId) {
        return eventRepository.existsById(eventId)
                .flatMap(exists -> {
                    if (exists) {
                        return eventRepository.findById(eventId).flatMap(fileEntity -> {
                            fileEntity.setStatus(Status.DELETED);
                            return eventRepository.save(fileEntity);
                        });
                    } else {
                        return Mono.error(new EventNotExistException(eventId));
                    }
                });
    }


}
