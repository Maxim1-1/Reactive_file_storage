package com.Maxim.File_storage_API.service;


import com.Maxim.File_storage_API.entity.*;
import com.Maxim.File_storage_API.exceptions.service_exceptions.UserNotExistException;
import com.Maxim.File_storage_API.repository.UserRepository;
import com.Maxim.File_storage_API.security.PBFDK2Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

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
        return userRepository.findAll();
    }

    public Mono<UserEntity> findUserById(Integer id) {
        return userRepository.existsById(id)
                .flatMap(exists -> {
                    if (exists) {
                        return userRepository.findById(id);
                    } else {
                        return Mono.error(new UserNotExistException(id));
                    }
                });
    }


    public Mono<UserEntity> saveUser(UserEntity user) {
        return userRepository.save(user);
    }

    public Mono<UserEntity> updateUserById(UserEntity updatedUser, Integer id) {
        return userRepository.existsById(id)
                .flatMap(exists -> {
                    if (exists) {
                        updatedUser.setId(id);
                        return userRepository.findById(id)
                                .map(userRepository -> {
                                    if (updatedUser.getName() != null & updatedUser.getName().equalsIgnoreCase(userRepository.getName())) {
                                        userRepository.setName(updatedUser.getName());
                                    }

                                    if (updatedUser.getStatus() != null & updatedUser.getStatus() != (userRepository.getStatus())) {
                                        userRepository.setStatus(updatedUser.getStatus());
                                    }
                                    return userRepository;
                                })
                                .flatMap(updatedEvent -> userRepository.save(updatedEvent));
                    } else {
                        return Mono.error(new UserNotExistException(id));
                    }
                });
    }


    public Mono<UserEntity> deleteUserById(Integer id) {
        return userRepository.existsById(id)
                .flatMap(exists -> {
                    if (exists) {
                        return userRepository.findById(id).flatMap(user -> {
                            user.setStatus(Status.DELETED);
                            return userRepository.save(user);
                        });
                    } else {
                        return Mono.error(new UserNotExistException(id));
                    }
                });
    }


    public Mono<UserEntity> getUserByUsername(String username) {
        return userRepository.findByName(username);
    }

}




