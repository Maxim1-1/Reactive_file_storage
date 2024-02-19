package com.Maxim.File_storage_API.service;


import com.Maxim.File_storage_API.entity.EventEntity;
import com.Maxim.File_storage_API.entity.FileEntity;
import com.Maxim.File_storage_API.entity.UserEntity;
import com.Maxim.File_storage_API.repository.EventRepository;
import com.Maxim.File_storage_API.repository.FileRepository;
import com.Maxim.File_storage_API.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        return userRepository.findAll()
                .flatMap(user -> Mono.zip(
                        Mono.just(user),
                        eventRepository.findAllByUserId(user.getId()).collectList()
                ).flatMap(tuples -> {
                    UserEntity currentUser = tuples.getT1();
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
                                currentUser.setEvents(updatedEventsList);
                                return currentUser;
                            });
                }));
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


    @Transactional
    public Mono<UserEntity> saveUser(UserEntity user) {
        return userRepository.save(user)
                .flatMap(savedUser -> {
                    Flux<EventEntity> eventFlux = user.getEvents() != null ? Flux.fromIterable(user.getEvents()) : Flux.empty();
                    return eventFlux.flatMap(event -> {
                        FileEntity file = event.getFile();
                        if (file == null) {
                            event.setFile(null);
                            return Mono.error(new RuntimeException("Файл не указан для события"));
                        }
                        return fileRepository.save(file)
                                .flatMap(fileEntity -> {
                                    return databaseClient.sql("INSERT INTO events (user_id, file_id, status) VALUES (:user_id, :file_id, :status)")
                                            .bind("user_id", savedUser.getId())
                                            .bind("file_id", fileEntity.getId())
                                            .bind("status", event.getStatus())
                                            .fetch()
                                            .rowsUpdated()
                                            .then(databaseClient.sql("SELECT MAX(id) FROM events WHERE user_id = :user_id")
                                                    .bind("user_id", savedUser.getId())
                                                    .map((row, rowMetadata) -> row.get(0, Integer.class))
                                                    .one()
                                                    .doOnNext(eventId -> event.setId(eventId))
                                                    .then(Mono.just(savedUser)));
                                });
                    }).then(Mono.just(savedUser));
                });
    }





    public Mono<UserEntity> updateUserById(UserEntity updatedUser) {
        return userRepository.findById(updatedUser.getId())
                .flatMap(existingUser -> {
                    if (updatedUser.getName() != null) {
                        existingUser.setName(updatedUser.getName());
                    }
                    if (updatedUser.getStatus() != null) {
                        existingUser.setStatus(updatedUser.getStatus());
                    }
                    if (updatedUser.getEvents() != null) {
                        existingUser.setEvents(updatedUser.getEvents());
                    }

                    for (EventEntity event : updatedUser.getEvents()) {

                        if (event.getFile() != null) {


                            eventRepository.updateEventByIdAllColumns(event.getId(), updatedUser.getId(), event.getFile().getId());
                        } else {
                            eventRepository.updateEventByIdAllColumns(event.getId(), updatedUser.getId(), null);
                        }
                    }

                    return userRepository.save(existingUser);
                });
    }

//    work
//    public Mono<UserEntity> up1dateUserById(UserEntity updatedUser) {
//        return userRepository.findById(updatedUser.getId())
//                .flatMap(existingUser -> {
//                    if (updatedUser.getName() != null) {
//                        existingUser.setName(updatedUser.getName());
//                    }
//                    if (updatedUser.getStatus() != null) {
//                        existingUser.setStatus(updatedUser.getStatus());
//                    }
//                    if (updatedUser.getEvents() != null) {
//                        return Flux.fromIterable(updatedUser.getEvents())
//                                .flatMap(event -> {
//                                    if (event.getFile() != null) {
//                                        return eventRepository.updateEventByIdAllColumns(event.getId(), updatedUser.getId(), event.getFile().getId()).then();
//                                    } else {
//                                        return eventRepository.updateEventByIdAllColumns(event.getId(), updatedUser.getId(), null).then();
//                                    }
//                                })
//                                .collectList()
//                                .flatMap(events -> {
//                                    existingUser.setEvents(updatedUser.getEvents());
//                                    return userRepository.save(existingUser);
//                                });
//                    } else {
//                        return userRepository.save(existingUser);
//                    }
//                });
//    }











}

// TODO уточнить надо ли при удалении юзера удалять связанные с ним Event, File, наверное так и сделаю ток не удалять а статус менять
//    public Mono<UserEntity> deleteUserById(UserEntity user){
//        if (userRepository.existsById(user.getId())) {
//
//        }
//        return userRepository.up(user);
//    }



