package com.Maxim.File_storage_API.controllers;

import com.Maxim.File_storage_API.entity.EventEntity;
import com.Maxim.File_storage_API.entity.FileEntity;
import com.Maxim.File_storage_API.entity.Status;
import com.Maxim.File_storage_API.entity.UserEntity;
import com.Maxim.File_storage_API.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserRestControllerV1 {

    @Autowired
    private UserService userService;


    @GetMapping("")
    public Mono<UserEntity> getUserById() {
        FileEntity file = new FileEntity();
        file.setFilePath("/pizdec");
        file.setCreateAt("bla");
        file.setName("blo");
        file.setUpdatedAt("fdf");
        file.setStatus(Status.ACTIVE);

        List<EventEntity> eventEntities = new ArrayList<>();

        EventEntity event = new EventEntity();
        event.setFile(file);
        event.setStatus(Status.ACTIVE);
        eventEntities.add(event);
        UserEntity user = new UserEntity();
        user.setName("ololo");
        user.setEvents(eventEntities);
        user.setStatus(Status.ACTIVE);

        return userService.saveUser(user);
    }

//    @GetMapping("")
//    public Flux<UserDTO> getAllUsers() {
//        UserDTO userDTO = new UserDTO();
//        userDTO.setId(1);
//
//        return Flux.just(userDTO);
//    }
//
//    @GetMapping("/{userId}")
//    public Mono<User> getUserById(@PathVariable Integer userId) {
//        return userService.getUserById(userId);
//    }
//
//    @DeleteMapping("/{userId}")
//    public Mono<Void> deleteUserById(@PathVariable Integer userId) {
//        return userService.deleteUserById(userId);
//    }
//
//    @PutMapping("/{userId}")
//    public Mono<User> updateUserById(@PathVariable Integer userId, @RequestBody User user) {
//        return userService.updateUserById(userId, user);
//    }
}
