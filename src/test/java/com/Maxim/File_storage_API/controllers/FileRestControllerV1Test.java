package com.Maxim.File_storage_API.controllers;

import com.Maxim.File_storage_API.dto.EventDTO;
import com.Maxim.File_storage_API.dto.FileDTO;
import com.Maxim.File_storage_API.dto.UserDTO;
import com.Maxim.File_storage_API.entity.EventEntity;
import com.Maxim.File_storage_API.entity.FileEntity;
import com.Maxim.File_storage_API.entity.Status;
import com.Maxim.File_storage_API.mapper.FileMapper;
import com.Maxim.File_storage_API.security.CustomPrincipal;
import com.Maxim.File_storage_API.service.FileService;
import com.Maxim.File_storage_API.service.FileUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@SpringBootTest
@AutoConfigureWebTestClient
class FileRestControllerV1Test {
    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private FileService fileService;
    @MockBean
    private FileUserService fileUserService;
    @MockBean
    Authentication authentication;

    @MockBean
    private FileMapper fileMapper;

    @Test
    @WithMockUser(authorities = "USER")
    void getFileById() {
        CustomPrincipal userDetails = new CustomPrincipal(1,"test");
        given(authentication.getPrincipal()).willReturn(userDetails);

        FileDTO testFileDto = new FileDTO();
        FileEntity file = new FileEntity();

        given(fileUserService.getFileByIdForRole(authentication.getAuthorities(), 1, 1)).willReturn(Mono.just(file));
        given(fileMapper.map(any(FileEntity.class))).willReturn(testFileDto);

        webTestClient.mutateWith(csrf()).get()
                .uri("/api/v1/files/1")
                .exchange()
                .expectStatus().isOk();

    }




































    @Test
    @WithMockUser(authorities = "USER")
    void updateFileByIdTestUserReturn403() {
        webTestClient.mutateWith(csrf()).put()
                .uri("/api/v1/files/")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateFileByIdTestAdminReturn200() {
        Integer fileId = 1;

        FileEntity file = new FileEntity();
        file.setName("test");
        file.setFilePath("/");
        file.setCreateAt("2020-01-01");
        file.setUpdatedAt("2020-01-01");
        file.setStatus(Status.DELETED);

        FileDTO fileDTO = new FileDTO();
        fileDTO.setName("test");
        fileDTO.setFilePath("/");
        fileDTO.setCreateAt("2020-01-01");
        fileDTO.setUpdatedAt("2020-01-01");
        fileDTO.setStatus(Status.DELETED);

        FileEntity outPutfile = new FileEntity();
        outPutfile.setId(fileId);
        outPutfile.setName("test");
        outPutfile.setFilePath("/");
        outPutfile.setCreateAt("2020-01-01");
        outPutfile.setUpdatedAt("2020-01-01");
        outPutfile.setStatus(Status.DELETED);

        FileDTO outPutfileDTO = new FileDTO();
        outPutfileDTO.setId(fileId);
        outPutfileDTO.setName("test");
        outPutfileDTO.setFilePath("/");
        outPutfileDTO.setCreateAt("2020-01-01");
        outPutfileDTO.setUpdatedAt("2020-01-01");
        outPutfileDTO.setStatus(Status.DELETED);

        given(fileMapper.map(any(FileDTO.class))).willReturn(file);
        given(fileService.updateFileById(file,fileId)).willReturn(Mono.just(outPutfile));
        given(fileMapper.map(any(FileEntity.class))).willReturn(fileDTO);

        webTestClient.mutateWith(csrf()).put()
                .uri("/api/v1/files/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(fileDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(FileDTO.class);
    }

    @Test
    @WithMockUser(authorities = "MODERATOR")
    void updateFileByIdTestModeratorReturn200() {
        Integer fileId = 1;

        FileEntity file = new FileEntity();
        file.setName("test");
        file.setFilePath("/");
        file.setCreateAt("2020-01-01");
        file.setUpdatedAt("2020-01-01");
        file.setStatus(Status.DELETED);

        FileDTO fileDTO = new FileDTO();
        fileDTO.setName("test");
        fileDTO.setFilePath("/");
        fileDTO.setCreateAt("2020-01-01");
        fileDTO.setUpdatedAt("2020-01-01");
        fileDTO.setStatus(Status.DELETED);

        FileEntity outPutfile = new FileEntity();
        outPutfile.setId(fileId);
        outPutfile.setName("test");
        outPutfile.setFilePath("/");
        outPutfile.setCreateAt("2020-01-01");
        outPutfile.setUpdatedAt("2020-01-01");
        outPutfile.setStatus(Status.DELETED);

        FileDTO outPutfileDTO = new FileDTO();
        outPutfileDTO.setId(fileId);
        outPutfileDTO.setName("test");
        outPutfileDTO.setFilePath("/");
        outPutfileDTO.setCreateAt("2020-01-01");
        outPutfileDTO.setUpdatedAt("2020-01-01");
        outPutfileDTO.setStatus(Status.DELETED);

        given(fileMapper.map(any(FileDTO.class))).willReturn(file);
        given(fileService.updateFileById(file,fileId)).willReturn(Mono.just(outPutfile));
        given(fileMapper.map(any(FileEntity.class))).willReturn(fileDTO);

        webTestClient.mutateWith(csrf()).put()
                .uri("/api/v1/files/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(fileDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(FileDTO.class);
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteFileByIdTestUserReturn403(){
        webTestClient.mutateWith(csrf()).delete()
                .uri("/api/v1/files")
                .exchange()
                .expectStatus().isForbidden();

    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteFileByIdTestAdminReturn200(){
        Integer fileId =1;

        FileEntity file = new FileEntity();
        file.setId(1);
        file.setName("test");
        file.setFilePath("/");
        file.setCreateAt("2020-01-01");
        file.setUpdatedAt("2020-01-01");
        file.setStatus(Status.DELETED);

        FileDTO fileDTO = new FileDTO();
        fileDTO.setId(1);
        fileDTO.setName("test");
        fileDTO.setFilePath("/");
        fileDTO.setCreateAt("2020-01-01");
        fileDTO.setUpdatedAt("2020-01-01");
        fileDTO.setStatus(Status.DELETED);

        given(fileService.deleteFileById(fileId)).willReturn(Mono.just(file));
        given(fileMapper.map(any(FileEntity.class))).willReturn(fileDTO);

        webTestClient.mutateWith(csrf()).delete()
                .uri("/api/v1/files/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(FileDTO.class)
                .value(expectedUser -> {
                    assertEquals(Status.DELETED, expectedUser.getStatus());
                });

    }

    @Test
    @WithMockUser(authorities = "MODERATOR")
    void deleteFileByIdTestModeratorReturn200(){
        Integer fileId =1;

        FileEntity file = new FileEntity();
        file.setId(1);
        file.setName("test");
        file.setFilePath("/");
        file.setCreateAt("2020-01-01");
        file.setUpdatedAt("2020-01-01");
        file.setStatus(Status.DELETED);

        FileDTO fileDTO = new FileDTO();
        fileDTO.setId(1);
        fileDTO.setName("test");
        fileDTO.setFilePath("/");
        fileDTO.setCreateAt("2020-01-01");
        fileDTO.setUpdatedAt("2020-01-01");
        fileDTO.setStatus(Status.DELETED);

        given(fileService.deleteFileById(fileId)).willReturn(Mono.just(file));
        given(fileMapper.map(any(FileEntity.class))).willReturn(fileDTO);

        webTestClient.mutateWith(csrf()).delete()
                .uri("/api/v1/files/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(FileDTO.class)
                .value(expectedUser -> {
                    assertEquals(Status.DELETED, expectedUser.getStatus());
                });

    }



}