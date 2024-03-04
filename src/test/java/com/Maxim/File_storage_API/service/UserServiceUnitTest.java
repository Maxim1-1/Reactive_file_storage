package com.Maxim.File_storage_API.service;

import com.Maxim.File_storage_API.entity.EventEntity;
import com.Maxim.File_storage_API.entity.Role;
import com.Maxim.File_storage_API.entity.Status;
import com.Maxim.File_storage_API.entity.UserEntity;
import com.Maxim.File_storage_API.exceptions.service_exceptions.UserNotExistException;
import com.Maxim.File_storage_API.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceUnitTest {

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserRepository userRepository;

    @Mock
    EventService eventService;

    @InjectMocks
    UserService userService;

    @Test
    void registerUserTestTestSuccess() {
        String password = "test";
        String passwordEncoded = "testEndcoded";

        UserEntity inputCreateUser = new UserEntity();
        inputCreateUser.setPassword(password);
        inputCreateUser.setRole(Role.USER);
        inputCreateUser.setName("test");

        UserEntity createUser = new UserEntity();
        createUser.setPassword(passwordEncoder.encode(inputCreateUser.getPassword()));
        createUser.setRole(inputCreateUser.getRole());
        createUser.setName(inputCreateUser.getName());
        createUser.setStatus(Status.ACTIVE);

        when(passwordEncoder.encode(password)).thenReturn(passwordEncoded);

        when(userRepository.save(any())).thenReturn(Mono.just(createUser));

        StepVerifier.create(userService.registerUser(inputCreateUser))
                .assertNext(u -> {
                    assertEquals("test", u.getName());
                })
                .verifyComplete();
    }

    @Test
    void findAllUsersTestSuccess() {
        UserEntity createUser = new UserEntity();
        createUser.setRole(Role.USER);
        createUser.setName("test");
        createUser.setStatus(Status.ACTIVE);

        UserEntity createUser2 = new UserEntity();
        createUser.setRole(Role.USER);
        createUser.setName("test");
        createUser.setStatus(Status.ACTIVE);

        when(userRepository.findAll()).thenReturn(Flux.just(createUser, createUser2));

        StepVerifier
                .create(userService.findAllUsers())
                .expectNext(createUser)
                .expectNext(createUser2)
                .verifyComplete();
    }

    @Test
    void findUserByIdTestSuccess() {

        Integer userId = 1;
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setName("test");
        userEntity.setRole(Role.USER);
        userEntity.setStatus(Status.ACTIVE);

        List<EventEntity> events = new ArrayList<>();
        EventEntity event = new EventEntity();

        events.add(event);


        when(eventService.findAllByUserID(userId)).thenReturn(Mono.just(events));
        when(userRepository.findById(userId)).thenReturn(Mono.just(userEntity));

        Mono<UserEntity> result = userService.findUserById(userId);

        StepVerifier.create(result)
                .expectNext(userEntity)
                .verifyComplete();
    }

    @Test
    public void testFindUserById_NonExistingUser() {
        Integer userId = 1;
        List<EventEntity> events = new ArrayList<>();
        EventEntity event = new EventEntity();
        events.add(event);

        when(eventService.findAllByUserID(userId)).thenReturn(Mono.just(events));
        when(userRepository.findById(userId)).thenReturn(Mono.empty());

        Mono<UserEntity> result = userService.findUserById(userId);

        result.subscribe(
                userEntity -> {
                    assertEquals(null, userEntity);
                },
                error -> {
                    assertEquals("UserNotExistException", error.getClass().getSimpleName());
                    UserNotExistException userNotExistException = (UserNotExistException) error;
                    assertEquals(userId, userNotExistException.getId());
                }
        );
        Mockito.verify(userRepository).findById(userId);
        Mockito.verify(eventService).findAllByUserID(userId);
    }

    @Test
    void saveUserSuccess() {
        Integer userId = 1;
        UserEntity user = new UserEntity();
        user.setRole(Role.USER);
        user.setName("test");

        UserEntity savedUser = new UserEntity();
        savedUser.setId(userId);
        savedUser.setName(user.getName());
        savedUser.setRole(Role.USER);

        when(userRepository.save(user)).thenReturn(Mono.just(savedUser));

        Mono<UserEntity> result = userService.saveUser(user);

        StepVerifier.create(userService.saveUser(user))
                .assertNext(u -> {
                    assertEquals("test", u.getName());
                    assertEquals(Role.USER, u.getRole());
                    assertEquals(userId, u.getId());
                })
                .verifyComplete();

    }

    @Test
    void updateUserByIdSuccess() {
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setName("test");
        user.setStatus(Status.ACTIVE);

        UserEntity updatedUser = new UserEntity();
        updatedUser.setId(1);
        updatedUser.setName("test_update");
        updatedUser.setStatus(Status.DELETED);

        when(userRepository.existsById(1)).thenReturn(Mono.just(true));
        when(userRepository.findById(1)).thenReturn(Mono.just(user));
        when(userRepository.save(user)).thenReturn(Mono.just(updatedUser));

        Mono<UserEntity> resultMono = userService.updateUserById(user, 1);

        StepVerifier.create(resultMono)
                .assertNext(result -> {
                    assertEquals(1, result.getId());
                    assertEquals("test_update", result.getName());
                    assertEquals(Status.DELETED, result.getStatus());
                })
                .verifyComplete();
    }

    @Test
    void updateUserById_NonExistingUser() {
        Integer userId = 1;
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setName("test");
        user.setStatus(Status.ACTIVE);

        when(userRepository.findById(userId)).thenReturn(Mono.empty());
        Mono<UserEntity> result = userService.updateUserById(user,userId);

        StepVerifier.create(result)
                .expectError(UserNotExistException.class)
                .verify();
    }

    @Test
    void deleteUserByIdSuccess() {
        Integer userId = 1;
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setName("test");
        userEntity.setRole(Role.USER);
        userEntity.setStatus(Status.ACTIVE);

        UserEntity deleteUser = new UserEntity();
        deleteUser.setId(userId);
        deleteUser.setName("test");
        deleteUser.setRole(Role.USER);
        deleteUser.setStatus(Status.DELETED);

        when(userRepository.existsById(userId)).thenReturn(Mono.just(true));
        when(userRepository.findById(userId)).thenReturn(Mono.just(userEntity));
        when(userRepository.save(userEntity)).thenReturn(Mono.just(deleteUser));

        StepVerifier.create(userService.deleteUserById(userId))
                .expectNextMatches(u -> Status.DELETED.equals(u.getStatus()))
                .verifyComplete();

    }
    @Test
    void deleteUserById_NonExistingUser() {
        Integer userId = 1;
        when(userRepository.findById(userId)).thenReturn(Mono.empty());
        Mono<UserEntity> result = userService.deleteUserById(userId);

        StepVerifier.create(result)
                .expectError(UserNotExistException.class)
                .verify();
    }
}