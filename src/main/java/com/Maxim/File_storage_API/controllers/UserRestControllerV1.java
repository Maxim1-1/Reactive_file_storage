package com.Maxim.File_storage_API.controllers;

import com.Maxim.File_storage_API.dto.UserDTO;
import com.Maxim.File_storage_API.entity.UserEntity;
import com.Maxim.File_storage_API.mapper.UserMapper;
import com.Maxim.File_storage_API.service.UserService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/v1/users")
public class UserRestControllerV1 {

    public UserRestControllerV1(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    private  UserService userService;

    private  UserMapper userMapper;


    @GetMapping("/{id}")
    public Mono<UserDTO> getUserById (@PathVariable Integer id) {
        return userService.findUserById(id).map(userMapper::map);
    }

    @GetMapping("")
    public Flux<UserDTO> getAllUsers() {
        return userService.findAllUsers().map(userMapper::map);
    }

    @PostMapping("")
    public Mono<UserDTO> saveUser(@RequestBody UserEntity user) {
        return userService.saveUser(user).map(userMapper::map);
    }

    @PutMapping("/{id}")
    public Mono<UserDTO> updateUserById(@PathVariable Integer id,@RequestBody UserEntity user) {
        return userService.updateUserById(user,id).map(userMapper::map);
    }


    @DeleteMapping("/{id}")
    public Mono<UserDTO> deleteUserById(@PathVariable Integer id) {
        return userService.deleteUserById(id).map(userMapper::map);
    }

}
