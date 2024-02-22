package com.Maxim.File_storage_API.controllers;

import com.Maxim.File_storage_API.entity.EventEntity;
import com.Maxim.File_storage_API.entity.FileEntity;
import com.Maxim.File_storage_API.entity.Status;
import com.Maxim.File_storage_API.entity.UserEntity;
import com.Maxim.File_storage_API.security.CustomPrincipal;
import com.Maxim.File_storage_API.service.FileService;
import com.Maxim.File_storage_API.service.FileUserService;
import com.Maxim.File_storage_API.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserRestControllerV1 {

    @Autowired
    private UserService userService;


    @GetMapping("/{id}")
    public Mono<UserEntity> getUserById (@PathVariable Integer id) {
        return userService.findUserById(id);
    }

    @GetMapping("")
    public Flux<UserEntity> getAllUsers() {
        return userService.findAllUsers();
    }

    @PostMapping("")
    public Mono<UserEntity> saveUser(@RequestBody UserEntity user) {
        return userService.saveUser(user);
    }

    @PutMapping("/{id}")
    public Mono<UserEntity> updateUserById(@PathVariable Integer id,@RequestBody UserEntity user) {
        user.setId(id);
        return userService.updateUserById(user);
    }

    @DeleteMapping("/{id}")
    public Mono<UserEntity> deleteUserById(@PathVariable Integer id) {
        return userService.deleteUserById(id);
    }

}
