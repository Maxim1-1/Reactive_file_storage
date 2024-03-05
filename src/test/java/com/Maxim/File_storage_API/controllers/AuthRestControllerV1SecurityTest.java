package com.Maxim.File_storage_API.controllers;

import com.Maxim.File_storage_API.dto.AuthRequestDto;
import com.Maxim.File_storage_API.dto.AuthResponseDto;
import com.Maxim.File_storage_API.dto.UserDTO;
import com.Maxim.File_storage_API.entity.Role;
import com.Maxim.File_storage_API.entity.Status;
import com.Maxim.File_storage_API.entity.UserEntity;
import com.Maxim.File_storage_API.exceptions.security_exeptions.InvalidCredentialsException;
import com.Maxim.File_storage_API.exceptions.security_exeptions.RegistrationError;
import com.Maxim.File_storage_API.exceptions.security_exeptions.UserNotAuthenticatedException;
import com.Maxim.File_storage_API.exceptions.service_exceptions.NotExistError;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;


@SpringBootTest
@AutoConfigureWebTestClient
public class AuthRestControllerV1SecurityTest {

    @MockBean
    private SecurityService securityService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @Autowired
    private WebTestClient webTestClient;

    @Test
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
                .bodyValue(userDTO)
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
    public void testRegisterUserForUnauthenticatedWithIncompleteDataReturn400() {
        UserDTO userDTO = new UserDTO();
        userDTO.setPassword("test");
        userDTO.setRole(Role.USER);

        UserEntity userEntity = new UserEntity();
        userEntity.setPassword("test");
        userEntity.setStatus(Status.ACTIVE);
        userEntity.setRole(Role.USER);

        given(userMapper.map(any(UserDTO.class))).willReturn(userEntity);
        given(userService.registerUser(userEntity)).willReturn(Mono.error(new RegistrationError()));

        webTestClient.mutateWith(csrf()).post()
                .uri("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userDTO)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(NotExistError.class)
                .value(expectedUser -> {
                    assertEquals("enter all required parameters: password, name, role", expectedUser.getMessage());
                });
    }

    @Test
    public void testLoginReturn200() throws ParseException {
        AuthRequestDto dto = new AuthRequestDto();
        dto.setPassword("test");
        dto.setName("test");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = sdf.parse("01/01/2022");

        AuthResponseDto response = new AuthResponseDto();
        response.setUserId(1);
        response.setIssuedAt(date);
        response.setToken("test");
        response.setExpiresAt(date);

        TokenDetails tokenDetails = new TokenDetails();
        tokenDetails.setUserId(1);
        tokenDetails.setIssuedAt(date);
        tokenDetails.setToken("test");
        tokenDetails.setExpiresAt(date);

        given(securityService.authenticate(dto.getName(),dto.getPassword())).willReturn(Mono.just(tokenDetails));

        webTestClient.mutateWith(csrf()).post()
                .uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthResponseDto.class)
                .value(expectedUser -> {
                    assertEquals("test", expectedUser.getToken());
                    assertEquals(1, expectedUser.getUserId());
                    assertEquals(date, expectedUser.getExpiresAt());
                    assertEquals(date, expectedUser.getIssuedAt());
                });
    }

    @Test
    public void testLoginNotPasswordReturn400() throws ParseException {
        AuthRequestDto dto = new AuthRequestDto();
        dto.setPassword("test");
        dto.setName("test");

        given(securityService.authenticate(dto.getName(),dto.getPassword())).willReturn(Mono.error(new InvalidCredentialsException("Invalid password")));

        webTestClient.mutateWith(csrf()).post()
                .uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(InvalidCredentialsException.class);
    }

    @Test
    public void testLoginWithDeletedUserReturn400() throws ParseException {
        AuthRequestDto dto = new AuthRequestDto();
        dto.setPassword("test");
        dto.setName("test");

        given(securityService.authenticate(dto.getName(),dto.getPassword())).willReturn(Mono.error(new UserNotAuthenticatedException("User deleted, authenticate is not possible")));

        webTestClient.mutateWith(csrf()).post()
                .uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(UserNotAuthenticatedException.class);
    }


    @Test
    public void testLoginNotUsernameUserReturn400() throws ParseException {
        AuthRequestDto dto = new AuthRequestDto();
        dto.setPassword("test");
        dto.setName("test");

        given(securityService.authenticate(dto.getName(),dto.getPassword())).willReturn(Mono.error(new InvalidCredentialsException("Invalid username")));

        webTestClient.mutateWith(csrf()).post()
                .uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(InvalidCredentialsException.class);
    }







}
