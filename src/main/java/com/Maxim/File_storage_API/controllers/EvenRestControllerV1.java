package com.Maxim.File_storage_API.controllers;

import com.Maxim.File_storage_API.dto.EventDTO;
import com.Maxim.File_storage_API.entity.EventEntity;
import com.Maxim.File_storage_API.mapper.EventMapper;
import com.Maxim.File_storage_API.security.CustomPrincipal;
import com.Maxim.File_storage_API.service.EventService;
import io.r2dbc.spi.R2dbcDataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/events")
public class EvenRestControllerV1 {

    public EvenRestControllerV1(EventService eventService, EventMapper eventMapper) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
    }

    private EventService eventService;
    private EventMapper eventMapper;


    @GetMapping("/{id}")
    public Mono<EventDTO> getEventById(@PathVariable Integer id) {
        return eventService.getEventById(id).map(eventMapper::map);
    }

    @GetMapping("")
    public Flux<EventDTO> getAllEvents() {
        return eventService.getAllEvents().map(eventMapper::map);
    }




    @PostMapping("")
    public Mono<ResponseEntity<EventDTO>> saveEvents(@RequestBody EventDTO event) {
        return eventService.saveEvents(eventMapper.map(event))
                .map(eventMapper::map)
                .map(ResponseEntity::ok)
                .onErrorResume(R2dbcDataIntegrityViolationException.class, e -> {
                    return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage()));
                });
    }


    @PutMapping("/{id}")
    public Mono<EventDTO> updateEventById(@PathVariable Integer id, @RequestBody EventDTO event) {
        event.setId(id);
        EventEntity eventEntity = eventMapper.map(event);
        return eventService.updateEventById(eventEntity).map(eventMapper::map);
    }

    @DeleteMapping("/{id}")
    public Mono<EventEntity> deleteEventById(@PathVariable Integer id) {
        return eventService.deleteEventById(id);
    }
}
