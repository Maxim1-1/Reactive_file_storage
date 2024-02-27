package com.Maxim.File_storage_API.service;

import com.Maxim.File_storage_API.entity.EventEntity;
import com.Maxim.File_storage_API.entity.FileEntity;
import com.Maxim.File_storage_API.entity.Status;
import com.Maxim.File_storage_API.exceptions.service_exceptions.EventNotExistException;
import com.Maxim.File_storage_API.exceptions.service_exceptions.FileNotExistException;
import com.Maxim.File_storage_API.exceptions.service_exceptions.UserAndFilesNotExistException;
import com.Maxim.File_storage_API.exceptions.service_exceptions.UserNotExistException;
import com.Maxim.File_storage_API.repository.EventRepository;
import com.Maxim.File_storage_API.repository.FileRepository;
import com.Maxim.File_storage_API.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class EventServiceUnitTest {
    @Mock
    EventRepository eventRepository;
    @Mock
    FileRepository fileRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    EventService eventService;

    @Test
    void findAllEventTestSuccess() {
        EventEntity event = new EventEntity();
        event.setStatus(Status.ACTIVE);

        EventEntity event1 = new EventEntity();
        event.setStatus(Status.ACTIVE);

        when(eventRepository.findAll()).thenReturn(Flux.just(event, event1));

        StepVerifier
                .create(eventService.getAllEvents())
                .expectNext(event)
                .expectNext(event1)
                .verifyComplete();
    }

    @Test
    void findEventByIdTestSuccess() {
        Integer eventId = 1;
        EventEntity eventEntity = new EventEntity();
        eventEntity.setId(eventId);
        eventEntity.setStatus(Status.ACTIVE);

        when(eventRepository.existsById(eventId)).thenReturn(Mono.just(true));
        when(eventRepository.findById(eventId)).thenReturn(Mono.just(eventEntity));

        Mono<EventEntity> result = eventService.getEventById(eventId);

        StepVerifier.create(result)
                .expectNext(eventEntity)
                .verifyComplete();
    }

    @Test
    public void testFindEventById_NonExistingFile() {
        Integer eventId = 1;
        when(eventRepository.existsById(eventId)).thenReturn(Mono.just(false));
        Mono<EventEntity> result = eventService.getEventById(eventId);
        result.subscribe(
                eventEntity -> {
                    assertEquals(null, eventEntity);
                },
                error -> {
                    assertEquals("EventNotExistException", error.getClass().getSimpleName());
                    EventNotExistException eventNotExistException = (EventNotExistException) error;
                    assertEquals(eventId, eventNotExistException.getId());
                }
        );
        Mockito.verify(eventRepository).existsById(eventId);
    }

    @Test
    void saveEventSuccess() {
        Integer eventId = 1;
        EventEntity event = new EventEntity();
        event.setUserId(123);
        event.setFileId(123);


        EventEntity expectedEventEntity = new EventEntity();
        expectedEventEntity.setId(eventId);
        expectedEventEntity.setFileId(event.getFileId());
        expectedEventEntity.setUserId(event.getUserId());
        expectedEventEntity.setStatus(Status.ACTIVE);

        when(fileRepository.existsById(event.getFileId())).thenReturn(Mono.just(true));
        when(userRepository.existsById(event.getUserId())).thenReturn(Mono.just(true));
        when(eventRepository.save(event)).thenReturn(Mono.just(expectedEventEntity));

        Mono<EventEntity> result = eventService.saveEvents(event);

        StepVerifier.create(result)
                .assertNext(eventEntity -> {
                    assertEquals(eventId, eventEntity.getId());
                    assertEquals(Status.ACTIVE, eventEntity.getStatus());
                    assertEquals(expectedEventEntity.getFileId(), eventEntity.getFileId());
                    assertEquals(expectedEventEntity.getUserId(), eventEntity.getUserId());

                })
                .verifyComplete();
    }


    @Test
    public void saveEventsUserDoesNotExist() {
        EventEntity event = new EventEntity();
        event.setUserId(100);
        event.setFileId(1);

        when(userRepository.existsById(event.getUserId())).thenReturn(Mono.just(false));
        when(fileRepository.existsById(event.getFileId())).thenReturn(Mono.just(true));

        StepVerifier.create(eventService.saveEvents(event))
                .expectError(UserNotExistException.class)
                .verify();
    }

    @Test
    public void saveEventsFileDoesNotExist() {
        EventEntity event = new EventEntity();
        event.setUserId(100);
        event.setFileId(1);

        when(userRepository.existsById(event.getUserId())).thenReturn(Mono.just(true));
        when(fileRepository.existsById(event.getFileId())).thenReturn(Mono.just(false));

        StepVerifier.create(eventService.saveEvents(event))
                .expectError(FileNotExistException.class)
                .verify();
    }

    @Test
    public void saveEventsFileAndUserDoesNotExist() {
        EventEntity event = new EventEntity();
        event.setUserId(100);
        event.setFileId(1);

        when(userRepository.existsById(event.getUserId())).thenReturn(Mono.just(false));
        when(fileRepository.existsById(event.getFileId())).thenReturn(Mono.just(false));

        StepVerifier.create(eventService.saveEvents(event))
                .expectError(UserAndFilesNotExistException.class)
                .verify();
    }

    @Test
    void updateEventByIdSuccess() {
        EventEntity event = new EventEntity();
        event.setUserId(1);
        event.setFileId(1);
        event.setId(1);
        event.setStatus(Status.ACTIVE);

        EventEntity updatedEvent = new EventEntity();
        updatedEvent.setFileId(2);
        updatedEvent.setUserId(2);
        updatedEvent.setId(1);
        updatedEvent.setStatus(Status.DELETED);

        when(fileRepository.existsById(1)).thenReturn(Mono.just(true));
        when(userRepository.existsById(1)).thenReturn(Mono.just(true));
        when(eventRepository.existsById(1)).thenReturn(Mono.just(true));
        when(eventRepository.findById(1)).thenReturn(Mono.just(event));
        when(eventRepository.save(event)).thenReturn(Mono.just(updatedEvent));

        Mono<EventEntity> resultMono = eventService.updateEventById(event, 1);

        StepVerifier.create(resultMono)
                .assertNext(result -> {
                    assertEquals(1, result.getId());
                    assertEquals(updatedEvent.getFileId(), result.getFileId());
                    assertEquals(updatedEvent.getUserId(), result.getUserId());

                    assertEquals(Status.DELETED, result.getStatus());
                })
                .verifyComplete();
    }

    @Test
    void updateEventById_NonExistingUser() {
        Integer eventId = 1;
        EventEntity event = new EventEntity();
        event.setId(1);
        event.setStatus(Status.ACTIVE);

        when(eventRepository.existsById(eventId)).thenReturn(Mono.just(false));
        Mono<EventEntity> result = eventService.updateEventById(event, eventId);

        StepVerifier.create(result)
                .expectError(EventNotExistException.class)
                .verify();
    }



    @Test
    void deleteEventByIdSuccess() {
        Integer eventId = 1;
        EventEntity event = new EventEntity();
        event.setId(eventId);
        event.setStatus(Status.ACTIVE);

        EventEntity expectedEvent = new EventEntity();
        expectedEvent.setId(eventId);
        expectedEvent.setStatus(Status.DELETED);

        when(eventRepository.existsById(eventId)).thenReturn(Mono.just(true));
        when(eventRepository.findById(eventId)).thenReturn(Mono.just(event));
        when(eventRepository.save(event)).thenReturn(Mono.just(expectedEvent));

        StepVerifier.create(eventService.deleteEventById(eventId))
                .expectNextMatches(u -> Status.DELETED.equals(u.getStatus()))
                .verifyComplete();

    }

    @Test
    void deleteEventById_NonExistingUser() {
        Integer eventId = 1;
        when(eventRepository.existsById(eventId)).thenReturn(Mono.just(false));
        Mono<EventEntity> result = eventService.deleteEventById(eventId);

        StepVerifier.create(result)
                .expectError(EventNotExistException.class)
                .verify();
    }
}