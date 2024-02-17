package com.Maxim.File_storage_API.controllers;

import com.Maxim.File_storage_API.entity.UserEntity;
import com.Maxim.File_storage_API.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserRestControllerV1 {

    @Autowired
    private UserService userService;


    @GetMapping("")
    public Mono<UserEntity> getUserById() {
//        Mono<fg> s = userService.findUserById(23);

        return userService.findUserById(23);
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
