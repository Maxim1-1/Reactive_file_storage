package com.Maxim.File_storage_API.service;


import com.Maxim.File_storage_API.entity.*;
import com.Maxim.File_storage_API.exceptions.security_exeptions.RegistrationError;
import com.Maxim.File_storage_API.exceptions.service_exceptions.UserNotExistException;
import com.Maxim.File_storage_API.repository.EventRepository;
import com.Maxim.File_storage_API.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UserService {


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EventService eventService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventService = eventService;
    }

    private final UserRepository userRepository;
    private final EventService eventService;

    private final PasswordEncoder passwordEncoder;

    public Mono<UserEntity> registerUser(UserEntity user) {
        if (user.getName()==null | user.getPassword()==null | user.getRole()==null) {
          return  Mono.error(new RegistrationError());
        }
        UserEntity createUser = new UserEntity();
        createUser.setPassword(passwordEncoder.encode(user.getPassword()));
        createUser.setRole(user.getRole());
        createUser.setName(user.getName());
        createUser.setStatus(Status.ACTIVE);
        return userRepository.save(createUser);
    }


    public Flux<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }

    public Mono<UserEntity> findUserById(Integer id) {
        return Mono.zip(userRepository.findById(id), eventService.findAllByUserID(id))
                .map(tuples -> {
                    UserEntity user = tuples.getT1();
                    List<EventEntity> events = tuples.getT2();
                    user.setEvents(events);
                    return user;
                }).switchIfEmpty(Mono.error(new UserNotExistException(id)));
    }


    public Mono<UserEntity> saveUser(UserEntity user) {
        return userRepository.save(user);
    }

    public Mono<UserEntity> updateUserById(UserEntity updatedUser, Integer id) {
        updatedUser.setId(id);
        return userRepository.findById(id)
                .flatMap(user -> {
                    if (updatedUser.getName() != null && updatedUser.getName().equalsIgnoreCase(user.getName())) {
                        user.setName(updatedUser.getName());
                    }

                    if (updatedUser.getStatus() != null && updatedUser.getStatus() != (user.getStatus())) {
                        user.setStatus(updatedUser.getStatus());
                    }
                    return userRepository.save(user);
                }).switchIfEmpty(Mono.error(new UserNotExistException(id)));
    }


    public Mono<UserEntity> deleteUserById(Integer id) {
        return userRepository.findById(id).flatMap(user -> {
            user.setStatus(Status.DELETED);
            return userRepository.save(user);
        }).switchIfEmpty(Mono.error(new UserNotExistException(id)));
    }


    public Mono<UserEntity> getUserByUsername(String username) {
        return userRepository.findByName(username);
    }

}




