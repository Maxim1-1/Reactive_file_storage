package com.Maxim.File_storage_API.controllers;

import com.Maxim.File_storage_API.dto.UserDTO;
import com.Maxim.File_storage_API.entity.UserEntity;
import com.Maxim.File_storage_API.mapper.UserMapper;
import com.Maxim.File_storage_API.security.SecurityService;
import com.Maxim.File_storage_API.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

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

    public void testRegisterUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("testUser");
        userDTO.setPassword("testPassword");

        UserEntity userEntity = new UserEntity();
        userEntity.setName("testUser");
        userEntity.setPassword("testPassword");

        given(userMapper.map(any(UserDTO.class))).willReturn(userEntity);
        given(userService.registerUser(any(UserEntity.class))).willReturn(Mono.just(userEntity));
        given(userMapper.map(any(UserEntity.class))).willReturn(userDTO);



        webTestClient.mutateWith(csrf()).post()
                .uri("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .value(dto -> assertEquals("testUser", dto.getName()));
    }
}
