package com.Maxim.File_storage_API.controllers;

import com.Maxim.File_storage_API.dto.AuthRequestDto;
import com.Maxim.File_storage_API.dto.AuthResponseDto;
import com.Maxim.File_storage_API.dto.UserDTO;
import com.Maxim.File_storage_API.entity.UserEntity;
import com.Maxim.File_storage_API.mapper.UserMapper;
import com.Maxim.File_storage_API.security.SecurityService;
import com.Maxim.File_storage_API.service.UserService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthRestControllerV1 {

    public AuthRestControllerV1(SecurityService securityService, UserService userService, UserMapper userMapper) {
        this.securityService = securityService;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    private  SecurityService securityService;
    private  UserService userService;
    private  UserMapper userMapper;


    @PostMapping("/register")
    public Mono<UserDTO> register(@RequestBody UserDTO dto) {
        UserEntity entity = userMapper.map(dto);
        return userService.registerUser(entity)
                .map(userMapper::map);
    }

    @PostMapping("/login")
    public Mono<AuthResponseDto> login(@RequestBody AuthRequestDto dto) {
        return securityService.authenticate(dto.getName(), dto.getPassword())
                .flatMap(tokenDetails -> {
                    AuthResponseDto response = new AuthResponseDto();
                    response.setUserId(tokenDetails.getUserId());
                    response.setIssuedAt(tokenDetails.getIssuedAt());
                    response.setToken(tokenDetails.getToken());
                    response.setExpiresAt(tokenDetails.getExpiresAt());
                    return Mono.just(response);

                });
    }
}
