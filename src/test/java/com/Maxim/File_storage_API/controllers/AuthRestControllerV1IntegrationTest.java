package com.Maxim.File_storage_API.controllers;

import com.Maxim.File_storage_API.dto.AuthRequestDto;
import com.Maxim.File_storage_API.dto.AuthResponseDto;
import com.Maxim.File_storage_API.dto.UserDTO;
import com.Maxim.File_storage_API.entity.Role;
import com.Maxim.File_storage_API.entity.Status;
import com.Maxim.File_storage_API.entity.UserEntity;
import com.Maxim.File_storage_API.mapper.UserMapper;
import com.Maxim.File_storage_API.security.SecurityService;
import com.Maxim.File_storage_API.security.TokenDetails;
import com.Maxim.File_storage_API.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;


@SpringBootTest
@AutoConfigureWebTestClient
public class AuthRestControllerV1IntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SecurityService securityService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @Test
//    @WithMockUser(username="testUser",roles="USER")
//    positive200
//    TODO добавить тест на валидацию необходимых параметров и кастомный exeption
    public void testRegisterUserForUnauthenticatedUserReturn200() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("testUser");
        userDTO.setPassword("test");
        userDTO.setRole(Role.USER);

        UserEntity userEntity = new UserEntity();
        userEntity.setName("testUser");
        userEntity.setPassword("test");
        userEntity.setStatus(Status.ACTIVE);
        userEntity.setRole(Role.USER);

        UserDTO outPutUserDTO = new UserDTO();
        outPutUserDTO.setId(1);
        outPutUserDTO.setName("testUser");
        outPutUserDTO.setPassword("test");
        outPutUserDTO.setRole(Role.USER);
        outPutUserDTO.setStatus(Status.ACTIVE);

        UserEntity outputuserEntity = new UserEntity();
        outputuserEntity.setId(1);
        outputuserEntity.setName("testUser");
        outputuserEntity.setPassword("test");
        outputuserEntity.setStatus(Status.ACTIVE);
        outputuserEntity.setRole(Role.USER);

        given(userMapper.map(any(UserDTO.class))).willReturn(userEntity);
        given(userService.registerUser(userEntity)).willReturn(Mono.just(outputuserEntity));
        given(userMapper.map(any(UserEntity.class))).willReturn(outPutUserDTO);

        webTestClient.mutateWith(csrf()).post()
                .uri("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(outPutUserDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .value(expectedUser -> {
                            assertEquals("testUser", expectedUser.getName());
                            assertEquals(1, expectedUser.getId());
                            assertEquals(null, expectedUser.getPassword());
                            assertEquals(Role.USER, expectedUser.getRole());
                            assertEquals(Status.ACTIVE, expectedUser.getStatus());
                        });

    }

    @Test
    public void testRegisterUserForUnauthenticatedAdminReturn200() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("testUser");
        userDTO.setPassword("test");
        userDTO.setRole(Role.ADMIN);

        UserEntity userEntity = new UserEntity();
        userEntity.setName("testUser");
        userEntity.setPassword("test");
        userEntity.setStatus(Status.ACTIVE);
        userEntity.setRole(Role.ADMIN);

        UserDTO outPutUserDTO = new UserDTO();
        outPutUserDTO.setId(1);
        outPutUserDTO.setName("testUser");
        outPutUserDTO.setPassword("test");
        outPutUserDTO.setRole(Role.ADMIN);
        outPutUserDTO.setStatus(Status.ACTIVE);

        UserEntity outputuserEntity = new UserEntity();
        outputuserEntity.setId(1);
        outputuserEntity.setName("testUser");
        outputuserEntity.setPassword("test");
        outputuserEntity.setStatus(Status.ACTIVE);
        outputuserEntity.setRole(Role.ADMIN);

        given(userMapper.map(any(UserDTO.class))).willReturn(userEntity);
        given(userService.registerUser(userEntity)).willReturn(Mono.just(outputuserEntity));
        given(userMapper.map(any(UserEntity.class))).willReturn(outPutUserDTO);

        webTestClient.mutateWith(csrf()).post()
                .uri("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(outPutUserDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .value(expectedUser -> {
                    assertEquals("testUser", expectedUser.getName());
                    assertEquals(1, expectedUser.getId());
                    assertEquals(null, expectedUser.getPassword());
                    assertEquals(Role.ADMIN, expectedUser.getRole());
                    assertEquals(Status.ACTIVE, expectedUser.getStatus());
                });

    }

    @Test
    public void testRegisterUserForUnauthenticatedModeratorReturn200() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("testUser");
        userDTO.setPassword("test");
        userDTO.setRole(Role.MODERATOR);

        UserEntity userEntity = new UserEntity();
        userEntity.setName("testUser");
        userEntity.setPassword("test");
        userEntity.setStatus(Status.ACTIVE);
        userEntity.setRole(Role.MODERATOR);

        UserDTO outPutUserDTO = new UserDTO();
        outPutUserDTO.setId(1);
        outPutUserDTO.setName("testUser");
        outPutUserDTO.setPassword("test");
        outPutUserDTO.setRole(Role.MODERATOR);
        outPutUserDTO.setStatus(Status.ACTIVE);

        UserEntity outputuserEntity = new UserEntity();
        outputuserEntity.setId(1);
        outputuserEntity.setName("testUser");
        outputuserEntity.setPassword("test");
        outputuserEntity.setStatus(Status.ACTIVE);
        outputuserEntity.setRole(Role.MODERATOR);

        given(userMapper.map(any(UserDTO.class))).willReturn(userEntity);
        given(userService.registerUser(userEntity)).willReturn(Mono.just(outputuserEntity));
        given(userMapper.map(any(UserEntity.class))).willReturn(outPutUserDTO);

        webTestClient.mutateWith(csrf()).post()
                .uri("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(outPutUserDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .value(expectedUser -> {
                    assertEquals("testUser", expectedUser.getName());
                    assertEquals(1, expectedUser.getId());
                    assertEquals(null, expectedUser.getPassword());
                    assertEquals(Role.MODERATOR, expectedUser.getRole());
                    assertEquals(Status.ACTIVE, expectedUser.getStatus());
                });

    }



}
