package com.Maxim.File_storage_API.repository;

import com.Maxim.File_storage_API.entity.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserEntity, Integer> {

    Mono<UserEntity> findByName(String username);

    //    @Query("SELECT * \n" +
//            "FROM user\n" +
//            "left join events on events.user_id = user.id\n" +
//            "left join files on events.file_id = files.id\n" +
//            "where user.id = :id;")
//    Mono<User> findUserById(Integer id);
//    @Query("SELECT user.id, user.name, user.status,\n" +
//            "events.id AS event_id, \n" +
//            "events.status AS event_status, \n" +
//            "files.id AS file_id, \n" +
//            "files.file_path, files.create_at, files.updated_at, files.status AS file_status \n" +
//            "FROM user\n" +
//            "left join events on events.user_id = user.id\n" +
//            "left join files on events.file_id = files.id\n" +
//            "where user.id = :id;")
//    Mono<fg> findUserById(Integer id);
//
//
//    @Query("SELECT user.id, user.name, user.status,\n" +
//            "events.id AS event_id, \n" +
//            "events.status AS event_status, \n" +
//            "files.id AS file_id, \n" +
//            "files.file_path, files.create_at, files.updated_at, files.status AS file_status \n" +
//            "FROM user\n" +
//            "left join events on events.user_id = user.id\n" +
//            "left join files on events.file_id = files.id\n" +
//            "where user.id = :id;")
//    Flux<fg> findUserByIdFlux(Integer id);

//    Mono<UserEntity> updateUserUsername(Long userId, String newUsername);

}
