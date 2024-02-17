package com.Maxim.File_storage_API.service;


import com.Maxim.File_storage_API.entity.EventEntity;
import com.Maxim.File_storage_API.entity.UserEntity;
import com.Maxim.File_storage_API.repository.EventRepository;
import com.Maxim.File_storage_API.repository.FileRepository;
import com.Maxim.File_storage_API.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private DatabaseClient databaseClient;

    public Flux<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }

    public Mono<UserEntity> findUserById(Integer id) {
        return Mono.zip(
                userRepository.findById(id),
                eventRepository.findAllByUserId(id).collectList()
        ).flatMap(tuples -> {
            UserEntity user = tuples.getT1();
            List<EventEntity> eventEntities = tuples.getT2();

            Flux<EventEntity> updatedEvents = Flux.fromIterable(eventEntities)
                    .flatMap(eventEntity -> databaseClient.sql("SELECT file_id FROM rest.events WHERE id = :eventId")
                            .bind("eventId", eventEntity.getId())
                            .map(row -> row.get("file_id", Integer.class))
                            .first()
                            .flatMap(fileId -> fileRepository.findById(fileId)
                                    .map(file -> {
                                        eventEntity.setFile(file);
                                        return eventEntity;
                                    })
                            )
                    );

            return updatedEvents.collectList()
                    .map(updatedEventsList -> {
                        user.setEvents(updatedEventsList);
                        return user;
                    });
        });
    }

//    public Mono<UserEntity> findUserById(Integer id) {
//
//        return Mono.zip(userRepository.findById(id), eventRepository.findAllByUserId(id).collectList())
//                .map(tuples -> {
//                    UserEntity user = tuples.getT1();
//                    List<EventEntity> eventEntities = tuples.getT2();
//
//                    eventEntities.stream().forEach(eventEntity -> {
//
//                                Mono<Integer> fileIdMono = databaseClient.sql("SELECT file_id FROM rest.events where id=event_id;")
//                                        .bind("event_id", eventEntity.getId())
//                                        .map((row -> row.get("file_id", Integer.class))).first();
//
//
//                                fileIdMono.flatMap(fileId -> fileRepository.findById(fileId)
//                                        .map(file->{
//                                            eventEntity.setFile(file);
//                                            return eventEntity;
//                                        })
//                                );
//                            }
//                            );
//                    user.setEvents(eventEntities);
//                    return user;
//
//
//                });
//    }
}
