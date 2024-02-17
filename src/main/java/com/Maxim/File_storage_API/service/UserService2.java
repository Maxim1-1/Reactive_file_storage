//package com.Maxim.File_storage_API.service;
//
//
//import com.Maxim.File_storage_API.entity.EventEntity;
//import com.Maxim.File_storage_API.entity.UserEntity;
//import com.Maxim.File_storage_API.repository.EventRepository;
//import com.Maxim.File_storage_API.repository.FileRepository;
//import com.Maxim.File_storage_API.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.r2dbc.core.DatabaseClient;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//
//@Service
//public class UserService2 {
//
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private EventRepository eventRepository;
//    @Autowired
//    private FileRepository fileRepository;
//
//    @Autowired
//    private DatabaseClient databaseClient;
//
//    public Flux<UserEntity> findAllUsers() {
//        return userRepository.findAll();
//    }
//
//
//
////        Mono<UserEntity> s = userRepository.findById(id)
////                .flatMap(UserEntity ->
////                        eventRepository.findById(UserEntity.getEvents()));
//
//
////        s.flatMapMany(result -> result.(
////                (row,rowMetadata) -> row.get("exists",Boolean.class)));
//
////
////        s.flatMap(user -> Mono.just(user)).doOnNext(
////                user -> {}).subscribe();
//
//
////    public Mono<UserEntity> findUserByIdFlux(Integer id) {
////
////
////        Mono<UserEntity> s = userRepository.findUserByIdFlux(id);
////
//////        s.flatMapMany(result -> result.(
//////                (row,rowMetadata) -> row.get("exists",Boolean.class)));
////
////
//////        s.flatMap(user -> Flux.just(user)).doOnNext(
//////                user -> {}).subscribe();
////
////        return s;
////    }
//
//
////
////    public User saveUser(User user) {
////        return userRepository.save(user);
////    }
////
////    public User getUserById(Integer userId) {
////        Optional<User> optionalUser = userRepository.findById(userId);
////        User user = optionalUser.get();
////        return user;
////    }
////
////    public List<User> getAllUsers() {
////        return userRepository.findAll();
////    }
////
////    public User updateUserById(User userId) {
////
//////TODO make it through, userRepository.existsById(id)
////// +  сделать сравнение полей юзера из бд, и полей из json
////        return null;
////    }
////
////    public void deleteUserById(Integer id) {
////        //TODO change status instead of delete
//////        userRepository.existsById(id)
////        userRepository.deleteById(id);
////    }
//
