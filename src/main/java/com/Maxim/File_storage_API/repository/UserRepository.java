package com.Maxim.File_storage_API.repository;

import com.Maxim.File_storage_API.entity.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserEntity,Integer> {

    Mono<UserEntity> findByName(String username);



}
