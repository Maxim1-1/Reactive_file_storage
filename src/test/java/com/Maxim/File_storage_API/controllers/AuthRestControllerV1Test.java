package com.Maxim.File_storage_API.controllers;

import com.Maxim.File_storage_API.dto.UserDTO;
import com.Maxim.File_storage_API.entity.Role;
import com.Maxim.File_storage_API.entity.Status;
import com.Maxim.File_storage_API.entity.UserEntity;
import com.Maxim.File_storage_API.mapper.UserMapper;
import com.Maxim.File_storage_API.security.SecurityService;
import com.Maxim.File_storage_API.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

//@ExtendWith(SpringExtension.class)
//@SpringBootTest()
@WebFluxTest(AuthRestControllerV1.class)
@AutoConfigureWebTestClient
public class AuthRestControllerV1Test {
    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private UserService userService;
    @MockBean
    private SecurityService securityService;
    @MockBean
    private UserMapper userMapper;

    @Test
    public void testRegisterValid() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("fssfsfsdxc");
        userDTO.setStatus(Status.ACTIVE);
        userDTO.setRole(Role.USER);
        userDTO.setPassword("test");

        UserEntity userEntity1 = new UserEntity();
        userEntity1.setName("fssfsfsdxc");
        userEntity1.setStatus(Status.ACTIVE);
        userEntity1.setRole(Role.USER);
        userEntity1.setPassword("test");

        UserEntity userEntity2 = new UserEntity();
        userEntity2.setName("fssfsfsdxc");
        userEntity2.setStatus(Status.ACTIVE);
        userEntity2.setRole(Role.USER);
        userEntity2.setPassword("test");
        userEntity2.setId(1);

        UserDTO userDTO2 = new UserDTO();
        userDTO.setName("fssfsfsdxc");
        userDTO.setStatus(Status.ACTIVE);
        userDTO.setRole(Role.USER);
        userDTO.setPassword("test");
        userDTO2.setId(1);


//        given(this.userService.registerUser(any(UserEntity.class)))
//                .willReturn(Mono.just(userEntity2));
        when(userService.registerUser(any(UserEntity.class)))
                .thenReturn(Mono.just(userEntity2));

        when(userMapper.map(userDTO)).thenReturn(userEntity1);
//        userService.registerUser(userEntity1);


//        when(userService.registerUser(any(UserEntity.class))).thenReturn(Mono.just(userEntity2));

//        when(userMapper.map(userEntity2)).thenReturn(Mono.just(userDTO2));



        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf()).
                post().uri("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ObjectMapper().writeValueAsString(userDTO))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .isEqualTo(userDTO);
    }
}
