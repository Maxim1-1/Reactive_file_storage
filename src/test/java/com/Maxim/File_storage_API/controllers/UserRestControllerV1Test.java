package com.Maxim.File_storage_API.controllers;

import com.Maxim.File_storage_API.dto.UserDTO;
import com.Maxim.File_storage_API.entity.Role;
import com.Maxim.File_storage_API.entity.Status;
import com.Maxim.File_storage_API.entity.UserEntity;
import com.Maxim.File_storage_API.exceptions.service_exceptions.UserNotExistException;
import com.Maxim.File_storage_API.mapper.UserMapper;
import com.Maxim.File_storage_API.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
@SpringBootTest
@AutoConfigureWebTestClient
class UserRestControllerV1Test {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(roles = "USER")
    void getUserByIdTestReturn403() {
        webTestClient.mutateWith(csrf()).get()
                .uri("/api/v1/users/1")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithMockUser(authorities={"ADMIN"})
    void getUserByIdTestAdminReturn200() {
        Integer userId = 1;

        UserEntity userEntity = new UserEntity();
        userEntity.setName("testUser");
        userEntity.setPassword("test");
        userEntity.setStatus(Status.ACTIVE);
        userEntity.setRole(Role.USER);

        UserDTO outPutUserDTO = new UserDTO();
        outPutUserDTO.setId(userId);
        outPutUserDTO.setName("testUser");
        outPutUserDTO.setPassword("test");
        outPutUserDTO.setRole(Role.USER);
        outPutUserDTO.setStatus(Status.ACTIVE);

        given(userService.findUserById(userId)).willReturn(Mono.just(userEntity));
        given(userMapper.map(any(UserEntity.class))).willReturn(outPutUserDTO);

        webTestClient.mutateWith(csrf()).get()
                .uri("/api/v1/users/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class);
    }

    @Test
    @WithMockUser(authorities={"MODERATOR"})
    void getUserByIdTestModeratorReturn200() {
        Integer userId = 1;

        UserEntity userEntity = new UserEntity();
        userEntity.setName("testUser");
        userEntity.setPassword("test");
        userEntity.setStatus(Status.ACTIVE);
        userEntity.setRole(Role.USER);

        UserDTO outPutUserDTO = new UserDTO();
        outPutUserDTO.setId(userId);
        outPutUserDTO.setName("testUser");
        outPutUserDTO.setPassword("test");
        outPutUserDTO.setRole(Role.USER);
        outPutUserDTO.setStatus(Status.ACTIVE);

        given(userService.findUserById(userId)).willReturn(Mono.just(userEntity));
        given(userMapper.map(any(UserEntity.class))).willReturn(outPutUserDTO);

        webTestClient.mutateWith(csrf()).get()
                .uri("/api/v1/users/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class);
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllUsersTestAdminReturn403() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("testUser");
        userEntity.setPassword("test");
        userEntity.setStatus(Status.ACTIVE);
        userEntity.setRole(Role.USER);

        UserEntity userEntity2 = new UserEntity();
        userEntity.setName("testUser2");
        userEntity.setPassword("test2");
        userEntity.setStatus(Status.ACTIVE);
        userEntity.setRole(Role.USER);

        given(userService.findAllUsers()).willReturn(Flux.just(userEntity,userEntity2));

        webTestClient.mutateWith(csrf()).get()
                .uri("/api/v1/users")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithMockUser(authorities={"ADMIN"})
    void getAllUsersTestAdminReturn200() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("testUser");
        userEntity.setPassword("test");
        userEntity.setStatus(Status.ACTIVE);
        userEntity.setRole(Role.USER);

        UserEntity userEntity2 = new UserEntity();
        userEntity.setName("testUser2");
        userEntity.setPassword("test2");
        userEntity.setStatus(Status.ACTIVE);
        userEntity.setRole(Role.USER);

        UserDTO userDto = new UserDTO();
        userDto.setName("testUser");
        userDto.setPassword("test");
        userDto.setStatus(Status.ACTIVE);
        userDto.setRole(Role.USER);

        UserDTO userDto2 = new UserDTO();
        userDto2.setName("testUser2");
        userDto2.setPassword("test2");
        userDto2.setStatus(Status.ACTIVE);
        userDto2.setRole(Role.USER);

        given(userService.findAllUsers()).willReturn(Flux.just(userEntity,userEntity2));

        given(userMapper.map(userEntity)).willReturn(userDto);
        given(userMapper.map(userEntity2)).willReturn(userDto2);

        webTestClient.mutateWith(csrf()).get()
                .uri("/api/v1/users")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @WithMockUser(authorities={"MODERATOR"})
    void getAllUsersTestModeratorReturn200() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("testUser");
        userEntity.setPassword("test");
        userEntity.setStatus(Status.ACTIVE);
        userEntity.setRole(Role.USER);

        UserEntity userEntity2 = new UserEntity();
        userEntity.setName("testUser2");
        userEntity.setPassword("test2");
        userEntity.setStatus(Status.ACTIVE);
        userEntity.setRole(Role.USER);

        UserDTO userDto = new UserDTO();
        userDto.setName("testUser");
        userDto.setPassword("test");
        userDto.setStatus(Status.ACTIVE);
        userDto.setRole(Role.USER);

        UserDTO userDto2 = new UserDTO();
        userDto2.setName("testUser2");
        userDto2.setPassword("test2");
        userDto2.setStatus(Status.ACTIVE);
        userDto2.setRole(Role.USER);

        given(userService.findAllUsers()).willReturn(Flux.just(userEntity,userEntity2));
        given(userMapper.map(userEntity)).willReturn(userDto);
        given(userMapper.map(userEntity2)).willReturn(userDto2);

        webTestClient.mutateWith(csrf()).get()
                .uri("/api/v1/users")
                .exchange()
                .expectStatus().isOk();
    }


    @Test
    @WithMockUser(roles = "USER")
    void saveUserTestUserReturn403() {
        webTestClient.mutateWith(csrf()).post()
                .uri("/api/v1/users")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    void saveUserTestModeratorReturn403() {
        webTestClient.mutateWith(csrf()).post()
                .uri("/api/v1/users")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void saveUserTestAdminReturn200() {
        UserDTO userDTOInput = new UserDTO();
        userDTOInput.setName("test123");
        userDTOInput.setPassword("test");
        userDTOInput.setRole(Role.USER);

        UserEntity entity = new UserEntity();
        entity.setName("test123");
        entity.setPassword("test");
        entity.setRole(Role.USER);

        UserEntity outPutUser = new UserEntity();
        outPutUser.setId(1);
        outPutUser.setName("test");
        outPutUser.setPassword("test");
        outPutUser.setRole(Role.USER);
        outPutUser.setStatus(Status.ACTIVE);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setName("test");
        userDTO.setPassword("test");
        userDTO.setRole(Role.USER);
        userDTO.setStatus(Status.ACTIVE);

        given(userMapper.map(any(UserDTO.class))).willReturn(entity);
        given(userService.saveUser(entity)).willReturn(Mono.just(outPutUser));
        given(userMapper.map(any(UserEntity.class))).willReturn(userDTO);

        webTestClient.mutateWith(csrf()).post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userDTOInput)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .value(expectedUser -> {
                    assertEquals(outPutUser.getName(), expectedUser.getName());
                    assertEquals(outPutUser.getId(), expectedUser.getId());
                    assertEquals(null, expectedUser.getPassword());
                    assertEquals(outPutUser.getRole(), expectedUser.getRole());
                    assertEquals(outPutUser.getStatus(), expectedUser.getStatus());
                });
    }

    @Test
    @WithMockUser(authorities = "USER")
    void updateUserByIdTestUserReturn403() {
        webTestClient.mutateWith(csrf()).put()
                .uri("/api/v1/users")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithMockUser(authorities = "MODERATOR")
    void updateUserByIdTestModeratorReturn403() {
        webTestClient.mutateWith(csrf()).put()
                .uri("/api/v1/users")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateUserByIdTestAdminReturn200() {
        Integer userId =1;

        UserDTO userDTOInput = new UserDTO();
        userDTOInput.setName("test123");
        userDTOInput.setPassword("test");
        userDTOInput.setRole(Role.USER);

        UserEntity userEntity = new UserEntity();
        userEntity.setName("test123");
        userEntity.setPassword("test");
        userEntity.setRole(Role.USER);

        UserEntity userEntity1 = new UserEntity();
        userEntity1.setId(1);
        userEntity1.setName("test123");
        userEntity1.setPassword("test");
        userEntity1.setRole(Role.USER);

        UserDTO outPutUserDTO = new UserDTO();
        outPutUserDTO.setId(1);
        outPutUserDTO.setName("test123");
        outPutUserDTO.setPassword("test");
        outPutUserDTO.setRole(Role.USER);

        given(userMapper.map(any(UserDTO.class))).willReturn(userEntity);
        given(userService.updateUserById(userEntity,userId)).willReturn(Mono.just(userEntity1));
        given(userMapper.map(any(UserEntity.class))).willReturn(outPutUserDTO);

        webTestClient.mutateWith(csrf()).put()
                .uri("/api/v1/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userDTOInput)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .value(expectedUser -> {
                    assertEquals(outPutUserDTO.getName(), expectedUser.getName());
                    assertEquals(outPutUserDTO.getId(), expectedUser.getId());
                    assertEquals(null, expectedUser.getPassword());
                    assertEquals(outPutUserDTO.getRole(), expectedUser.getRole());
                    assertEquals(outPutUserDTO.getStatus(), expectedUser.getStatus());
                });
    }

    @Test
    @WithMockUser(authorities = "USER")
    void deleteUserByIdTestUserReturn403() {
        webTestClient.mutateWith(csrf()).delete()
                .uri("/api/v1/users/1")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithMockUser(authorities = "MODERATOR")
    void deleteUserByIdTestModeratorReturn403() {
        webTestClient.mutateWith(csrf()).delete()
                .uri("/api/v1/users/1")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteUserByIdTestAdminReturn200() {
        Integer userId =1;

        UserEntity userEntity1 = new UserEntity();
        userEntity1.setId(userId);
        userEntity1.setName("test123");
        userEntity1.setPassword("test");
        userEntity1.setRole(Role.USER);
        userEntity1.setStatus(Status.DELETED);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setName("test123");
        userDTO.setPassword("test");
        userDTO.setRole(Role.USER);
        userDTO.setStatus(Status.DELETED);

        given(userService.deleteUserById(userId)).willReturn(Mono.just(userEntity1));
        given(userMapper.map(any(UserEntity.class))).willReturn(userDTO);

        webTestClient.mutateWith(csrf()).delete()
                .uri("/api/v1/users/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .value(expectedUser -> {
                    assertEquals(Status.DELETED, expectedUser.getStatus());
                });
    }
}