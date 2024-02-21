package com.Maxim.File_storage_API.service;


import com.Maxim.File_storage_API.entity.*;
import com.Maxim.File_storage_API.repository.EventRepository;
import com.Maxim.File_storage_API.repository.FileRepository;
import com.Maxim.File_storage_API.repository.UserRepository;
import com.Maxim.File_storage_API.security.PBFDK2Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UserService  {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private FileRepository fileRepository;

    private FileService fileService;

    @Autowired
    private EventService eventService = new EventService();

    @Autowired
    private DatabaseClient databaseClient;

    private final PasswordEncoder passwordEncoder = new PBFDK2Encoder();

    public Mono<UserEntity> registerUser(UserEntity user) {
        UserEntity createUser = new UserEntity();

        createUser.setPassword(passwordEncoder.encode(user.getPassword()));
        createUser.setRole(user.getRole());
        createUser.setName(user.getName());
        createUser.setStatus(Status.ACTIVE);

        return userRepository.save(createUser);


    }





    public Flux<UserEntity> findAllUsers() {
        return userRepository.findAll()
                .flatMap(user -> Mono.zip(
                        Mono.just(user),
                        eventRepository.findAllIdRelatedEventsByUserId(user.getId()).collectList()
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
                eventRepository.findAllIdRelatedEventsByUserId(id).collectList()
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
                            return Mono.error(new RuntimeException("Файл для события не указан"));
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

    @Transactional
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
                        return Flux.fromIterable(updatedUser.getEvents())
                                .flatMap(event -> {
                                    if (event.getFile() != null) {
                                        return eventRepository.updateEventByIdAllColumns(event.getId(), updatedUser.getId(), event.getFile().getId(), event.getStatus()).then();
                                    } else {
                                        return eventRepository.updateEventByIdAllColumns(event.getId(), updatedUser.getId(), null, event.getStatus()).then();
                                    }
                                })
                                .collectList()
                                .flatMap(events -> {
                                    existingUser.setEvents(updatedUser.getEvents());
                                    return userRepository.save(existingUser);
                                });
                    } else {
                        return userRepository.save(existingUser);
                    }
                });
    }




    @Transactional
    public Mono<UserEntity> deleteUserById(Integer id) {

        return userRepository.findById(id)
                .flatMap(userEntity -> {
                    userEntity.setStatus(Status.DELETED);

                    return eventRepository.findAllIdRelatedEventsByUserId(id)
                            .flatMap(event -> eventService.getEventById(event.getId())
                                    .flatMap(eventWithFile -> {
                                        if (eventWithFile.getFile() != null) {
                                            return fileRepository.updateFileStatus(eventWithFile.getFile().getId(), Status.DELETED)
                                                    .thenReturn(eventWithFile);
                                        } else {
                                            return Mono.just(eventWithFile);
                                        }
                                    })
                                    .flatMap(eventWithFile -> {
                                        return eventRepository.updateEventStatus(eventWithFile.getId(), Status.DELETED);
                                    })
                                    .then()
                            )
                            .then(userRepository.save(userEntity));
                });
    }


    public Mono<UserEntity> getUserByUsername(String username) {
        return userRepository.findByName(username);
    }

}




