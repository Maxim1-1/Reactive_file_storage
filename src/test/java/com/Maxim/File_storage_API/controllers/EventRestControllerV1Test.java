package com.Maxim.File_storage_API.controllers;

import com.Maxim.File_storage_API.dto.EventDTO;
import com.Maxim.File_storage_API.dto.UserDTO;
import com.Maxim.File_storage_API.entity.EventEntity;
import com.Maxim.File_storage_API.entity.Role;
import com.Maxim.File_storage_API.entity.Status;
import com.Maxim.File_storage_API.entity.UserEntity;
import com.Maxim.File_storage_API.mapper.EventMapper;
import com.Maxim.File_storage_API.service.EventService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@SpringBootTest
@AutoConfigureWebTestClient
class EventRestControllerV1Test {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private EventMapper eventMapper;

    @MockBean
    private EventService eventService;

    @Test
    @WithMockUser(roles = "USER")
    void getEventByIdTestReturn403() {
        webTestClient.mutateWith(csrf()).get()
                .uri("/api/v1/events/1")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithMockUser(authorities={"ADMIN"})
    void getEventByIdTestAdminReturn200() {
        Integer eventId = 1;

        EventEntity event = new EventEntity();
        event.setFileId(1);
        event.setUserId(1);
        event.setStatus(Status.ACTIVE);

        EventDTO outPutEventTO = new EventDTO();
        outPutEventTO.setFileId(1);
        outPutEventTO.setUserId(1);
        outPutEventTO.setStatus(Status.ACTIVE);

        given(eventService.getEventById(eventId)).willReturn(Mono.just(event));
        given(eventMapper.map(any(EventEntity.class))).willReturn(outPutEventTO);

        webTestClient.mutateWith(csrf()).get()
                .uri("/api/v1/events/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class);
    }

    @Test
    @WithMockUser(authorities={"MODERATOR"})
    void getEventByIdTestModeratorReturn200() {
        Integer eventId = 1;

        EventEntity event = new EventEntity();
        event.setFileId(1);
        event.setUserId(1);
        event.setStatus(Status.ACTIVE);

        EventDTO outPutEventTO = new EventDTO();
        outPutEventTO.setFileId(1);
        outPutEventTO.setUserId(1);
        outPutEventTO.setStatus(Status.ACTIVE);

        given(eventService.getEventById(eventId)).willReturn(Mono.just(event));
        given(eventMapper.map(any(EventEntity.class))).willReturn(outPutEventTO);

        webTestClient.mutateWith(csrf()).get()
                .uri("/api/v1/events/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class);
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllUsersTestAdminReturn403() {
        EventEntity event = new EventEntity();
        event.setFileId(1);
        event.setUserId(1);
        event.setStatus(Status.ACTIVE);

        EventEntity event2 = new EventEntity();
        event.setFileId(2);
        event.setUserId(2);
        event.setStatus(Status.ACTIVE);

        given(eventService.getAllEvents()).willReturn(Flux.just(event,event2));

        webTestClient.mutateWith(csrf()).get()
                .uri("/api/v1/events/")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithMockUser(authorities={"ADMIN"})
    void getAllEventsTestAdminReturn200() {
        EventEntity event = new EventEntity();
        event.setFileId(1);
        event.setUserId(1);
        event.setStatus(Status.ACTIVE);

        EventEntity event2 = new EventEntity();
        event.setFileId(2);
        event.setUserId(2);
        event.setStatus(Status.ACTIVE);

        EventDTO eventDTO = new EventDTO();
        eventDTO.setFileId(1);
        eventDTO.setUserId(1);
        eventDTO.setStatus(Status.ACTIVE);

        EventDTO eventDTO2 = new EventDTO();
        eventDTO.setFileId(2);
        eventDTO.setUserId(2);
        eventDTO.setStatus(Status.ACTIVE);



        given(eventService.getAllEvents()).willReturn(Flux.just(event, event2));

        given(eventMapper.map(event)).willReturn(eventDTO);
        given(eventMapper.map(event2)).willReturn(eventDTO2);

        webTestClient.mutateWith(csrf()).get()
                .uri("/api/v1/events")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @WithMockUser(authorities={"MODERATOR"})
    void getAllEventsTestModeratorReturn200() {
        EventEntity event = new EventEntity();
        event.setFileId(1);
        event.setUserId(1);
        event.setStatus(Status.ACTIVE);

        EventEntity event2 = new EventEntity();
        event.setFileId(2);
        event.setUserId(2);
        event.setStatus(Status.ACTIVE);

        EventDTO eventDTO = new EventDTO();
        eventDTO.setFileId(1);
        eventDTO.setUserId(1);
        eventDTO.setStatus(Status.ACTIVE);

        EventDTO eventDTO2 = new EventDTO();
        eventDTO.setFileId(2);
        eventDTO.setUserId(2);
        eventDTO.setStatus(Status.ACTIVE);



        given(eventService.getAllEvents()).willReturn(Flux.just(event, event2));

        given(eventMapper.map(event)).willReturn(eventDTO);
        given(eventMapper.map(event2)).willReturn(eventDTO2);

        webTestClient.mutateWith(csrf()).get()
                .uri("/api/v1/events")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @WithMockUser(roles = "USER")
    void saveEventTestModeratorReturn403() {
        webTestClient.mutateWith(csrf()).post()
                .uri("/api/v1/events")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void saveEventTestAdminReturn200() {
        EventEntity event = new EventEntity();
        event.setUserId(1);
        event.setFileId(1);
        event.setStatus(Status.ACTIVE);

        EventDTO eventDTO = new EventDTO();
        eventDTO.setUserId(1);
        eventDTO.setFileId(1);
        eventDTO.setStatus(Status.ACTIVE);

        EventEntity eventOutPut = new EventEntity();
        eventOutPut.setId(1);
        eventOutPut.setUserId(1);
        eventOutPut.setFileId(1);
        eventOutPut.setStatus(Status.ACTIVE);

        EventDTO eventOutPutDTO = new EventDTO();
        eventOutPutDTO.setId(1);
        eventOutPutDTO.setUserId(1);
        eventOutPutDTO.setFileId(1);
        eventOutPutDTO.setStatus(Status.ACTIVE);

        given(eventMapper.map(any(EventDTO.class))).willReturn(event);
        given(eventService.saveEvents(event)).willReturn(Mono.just(eventOutPut));
        given(eventMapper.map(any(EventEntity.class))).willReturn(eventOutPutDTO);

        webTestClient.mutateWith(csrf()).post()
                .uri("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(eventDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EventDTO.class)
                .value(expectedUser -> {
                    assertEquals(eventOutPutDTO.getFileId(), expectedUser.getFileId());
                    assertEquals(eventOutPutDTO.getUserId(), expectedUser.getUserId());
                });
    }

    @Test
    @WithMockUser(authorities = "MODERATOR")
    void saveEventTestModeratorReturn200() {

        EventEntity event = new EventEntity();
        event.setUserId(1);
        event.setFileId(1);
        event.setStatus(Status.ACTIVE);

        EventDTO eventDTO = new EventDTO();
        eventDTO.setUserId(1);
        eventDTO.setFileId(1);
        eventDTO.setStatus(Status.ACTIVE);

        EventEntity eventOutPut = new EventEntity();
        eventOutPut.setId(1);
        eventOutPut.setUserId(1);
        eventOutPut.setFileId(1);
        eventOutPut.setStatus(Status.ACTIVE);

        EventDTO eventOutPutDTO = new EventDTO();
        eventOutPutDTO.setId(1);
        eventOutPutDTO.setUserId(1);
        eventOutPutDTO.setFileId(1);
        eventOutPutDTO.setStatus(Status.ACTIVE);

        given(eventMapper.map(any(EventDTO.class))).willReturn(event);
        given(eventService.saveEvents(event)).willReturn(Mono.just(eventOutPut));
        given(eventMapper.map(any(EventEntity.class))).willReturn(eventOutPutDTO);

        webTestClient.mutateWith(csrf()).post()
                .uri("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(eventDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EventDTO.class)
                .value(expectedUser -> {
                    assertEquals(eventOutPutDTO.getFileId(), expectedUser.getFileId());
                    assertEquals(eventOutPutDTO.getUserId(), expectedUser.getUserId());
                });
    }

    @Test
    @WithMockUser(authorities = "USER")
    void updateEventByIdTestUserReturn403() {
        webTestClient.mutateWith(csrf()).put()
                .uri("/api/v1/events")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateEventByIdTestAdminReturn200() {
        Integer eventId = 1;
        EventEntity event = new EventEntity();
        event.setUserId(1);
        event.setFileId(1);
        event.setStatus(Status.ACTIVE);

        EventDTO eventDTO = new EventDTO();
        eventDTO.setUserId(1);
        eventDTO.setFileId(1);
        eventDTO.setStatus(Status.ACTIVE);

        EventEntity eventOutPut = new EventEntity();
        eventOutPut.setId(1);
        eventOutPut.setUserId(1);
        eventOutPut.setFileId(1);
        eventOutPut.setStatus(Status.ACTIVE);

        EventDTO eventOutPutDTO = new EventDTO();
        eventOutPutDTO.setId(1);
        eventOutPutDTO.setUserId(1);
        eventOutPutDTO.setFileId(1);
        eventOutPutDTO.setStatus(Status.ACTIVE);

        given(eventMapper.map(any(EventDTO.class))).willReturn(event);
        given(eventService.updateEventById(event,eventId)).willReturn(Mono.just(eventOutPut));
        given(eventMapper.map(any(EventEntity.class))).willReturn(eventOutPutDTO);

        webTestClient.mutateWith(csrf()).put()
                .uri("/api/v1/events/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(eventDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EventDTO.class)
                .value(expectedUser -> {
                    assertEquals(eventOutPutDTO.getFileId(), expectedUser.getFileId());
                    assertEquals(eventOutPutDTO.getUserId(), expectedUser.getUserId());
                });
    }

    @Test
    @WithMockUser(authorities = "MODERATOR")
    void updateEventByIdTestModeratorReturn200() {
        Integer eventId = 1;
        EventEntity event = new EventEntity();
        event.setUserId(1);
        event.setFileId(1);
        event.setStatus(Status.ACTIVE);

        EventDTO eventDTO = new EventDTO();
        eventDTO.setUserId(1);
        eventDTO.setFileId(1);
        eventDTO.setStatus(Status.ACTIVE);

        EventEntity eventOutPut = new EventEntity();
        eventOutPut.setId(1);
        eventOutPut.setUserId(1);
        eventOutPut.setFileId(1);
        eventOutPut.setStatus(Status.ACTIVE);

        EventDTO eventOutPutDTO = new EventDTO();
        eventOutPutDTO.setId(1);
        eventOutPutDTO.setUserId(1);
        eventOutPutDTO.setFileId(1);
        eventOutPutDTO.setStatus(Status.ACTIVE);

        given(eventMapper.map(any(EventDTO.class))).willReturn(event);
        given(eventService.updateEventById(event,eventId)).willReturn(Mono.just(eventOutPut));
        given(eventMapper.map(any(EventEntity.class))).willReturn(eventOutPutDTO);

        webTestClient.mutateWith(csrf()).put()
                .uri("/api/v1/events/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(eventDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EventDTO.class)
                .value(expectedUser -> {
                    assertEquals(eventOutPutDTO.getFileId(), expectedUser.getFileId());
                    assertEquals(eventOutPutDTO.getUserId(), expectedUser.getUserId());
                });
    }

    @Test
    @WithMockUser(authorities = "USER")
    void deleteEventByIdTestUserReturn403() {
        webTestClient.mutateWith(csrf()).delete()
                .uri("/api/v1/events")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteEventByIdTestAdminReturn200() {
        Integer eventId =1;

        EventEntity event = new EventEntity();
        event.setId(1);
        event.setFileId(1);
        event.setUserId(1);
        event.setStatus(Status.DELETED);

        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(1);
        eventDTO.setFileId(1);
        eventDTO.setUserId(1);
        eventDTO.setStatus(Status.DELETED);

        given(eventService.deleteEventById(eventId)).willReturn(Mono.just(event));
        given(eventMapper.map(any(EventEntity.class))).willReturn(eventDTO);

        webTestClient.mutateWith(csrf()).delete()
                .uri("/api/v1/events/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .value(expectedUser -> {
                    assertEquals(Status.DELETED, expectedUser.getStatus());
                });
    }

    @Test
    @WithMockUser(authorities = "MODERATOR")
    void deleteEventByIdTestModeratorReturn200() {
        Integer eventId =1;

        EventEntity event = new EventEntity();
        event.setId(1);
        event.setFileId(1);
        event.setUserId(1);
        event.setStatus(Status.DELETED);

        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(1);
        eventDTO.setFileId(1);
        eventDTO.setUserId(1);
        eventDTO.setStatus(Status.DELETED);

        given(eventService.deleteEventById(eventId)).willReturn(Mono.just(event));
        given(eventMapper.map(any(EventEntity.class))).willReturn(eventDTO);

        webTestClient.mutateWith(csrf()).delete()
                .uri("/api/v1/events/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .value(expectedUser -> {
                    assertEquals(Status.DELETED, expectedUser.getStatus());
                });
    }



}