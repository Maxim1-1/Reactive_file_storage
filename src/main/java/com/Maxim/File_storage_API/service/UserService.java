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

//    public Mono<UserEntity> saveUser(UserEntity user) {
//
//      return   userRepository.save(user).
//                flatMap( userEntity -> {
//                    for (EventEntity event: user.getEvents()) {
//                        Mono<FileEntity> savedFile = fileRepository.save(event.getFile());
//
//                        savedFile.flatMap(fileEntity -> {
//                            databaseClient.sql("INSERT INTO events (user_id, file_id) VALUES (:user_id, :file_id, ':status);")
//                                    .bind("user_id",user.getId())
//                                    .bind("file_id",fileEntity.getId())
//                                    .bind("status",event.getStatus())
//                                    .fetch()
//                                    .rowsUpdated()
//                                    .then(Mono.just(userEntity));
//
//
//                                }
//                        );
//
//                    }
//                    return userEntity;
//                }
//
//        );
//    }

    public Mono<UserEntity> saveUser(UserEntity user) {
        return userRepository.save(user)
                .flatMap(savedUser -> {
                    return Flux.fromIterable(user.getEvents())
                            .flatMap(event -> {
                                return fileRepository.save(event.getFile())
                                        .flatMap(fileEntity -> {
                                            return databaseClient.sql("INSERT INTO events (user_id, file_id, status) VALUES (:user_id, :file_id, :status)")
                                                    .bind("user_id", savedUser.getId())
                                                    .bind("file_id", fileEntity.getId())
                                                    .bind("status", event.getStatus())
                                                    .fetch()
                                                    .rowsUpdated()
                                                    .then(Mono.just(savedUser));
                                        });
                            })
                            .then(Mono.just(savedUser));
                });
    }


//    public Mono<UserEntity> updateUserById(UserEntity updatedUser) {
//        return userRepository.findById(updatedUser.getId())
//                .flatMap(existingUser -> {
////                    TODO дописать сравнение аналогичных параметров
//                    if (existingUser.getName().equalsIgnoreCase(updatedUser.getName())){}
//                    existingUser.setName(updatedUser.getName());
////                    TODo заменить save ниже на saveUser
//                    return saveUser(existingUser);
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



