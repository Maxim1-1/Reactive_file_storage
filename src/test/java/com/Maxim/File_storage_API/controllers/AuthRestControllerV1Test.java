package com.Maxim.File_storage_API.controllers;

import com.Maxim.File_storage_API.dto.UserDTO;
import com.Maxim.File_storage_API.entity.Role;
import com.Maxim.File_storage_API.entity.Status;
import com.Maxim.File_storage_API.entity.UserEntity;
import com.Maxim.File_storage_API.mapper.UserMapper;
import com.Maxim.File_storage_API.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureWebTestClient
public class AuthRestControllerV1Test {

    @InjectMocks
    private AuthRestControllerV1 authController;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Test
    public void testRegister() {
        UserDTO userDTO = new UserDTO();
        userDTO.setRole(Role.USER);
        userDTO.setStatus(Status.ACTIVE);
        userDTO.setName("test");
        userDTO.setPassword("test");

        UserEntity userEntity = new UserEntity();
        userEntity.setRole(Role.USER);
        userEntity.setStatus(Status.ACTIVE);
        userEntity.setName("test");
        userEntity.setPassword("test");


        UserEntity userEntity1 = new UserEntity();
        userEntity1.setId(1);
        userEntity1.setRole(Role.USER);
        userEntity1.setStatus(Status.ACTIVE);
        userEntity1.setName("test");
        userEntity1.setPassword("test");


        UserDTO userDTO1 = new UserDTO();
        userDTO1.setId(1);
        userDTO.setRole(Role.USER);
        userDTO.setStatus(Status.ACTIVE);
        userDTO.setName("test");
        userDTO.setPassword("test");

        Mockito.when(userMapper.map(userDTO)).thenReturn(userEntity);
        Mockito.when(userService.registerUser(userEntity)).thenReturn(Mono.just(userEntity1));
        Mockito.when(userMapper.map(userEntity)).thenReturn(userDTO1);

        Mono<UserDTO> result = authController.register(userDTO);

        // Assert
        result.subscribe(dto -> {
            assertEquals("test", dto.getName());
            assertEquals(1, dto.getId());
            // Add assertions here to verify the returned UserDTO
        });
    }
}

