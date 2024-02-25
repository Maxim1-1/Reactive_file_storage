package com.Maxim.File_storage_API.controllers;

import com.Maxim.File_storage_API.dto.EventDTO;
import com.Maxim.File_storage_API.entity.EventEntity;
import com.Maxim.File_storage_API.mapper.EventMapper;
import com.Maxim.File_storage_API.service.EventService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/events")
public class EventRestControllerV1 {
    public EventRestControllerV1(EventService eventService, EventMapper eventMapper) {
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
    public Mono<EventDTO> saveEvents(@RequestBody EventDTO event) {
        return eventService.saveEvents(eventMapper.map(event))
                .map(eventMapper::map);
    }

    @PutMapping("/{id}")
    public Mono<EventDTO> updateEventById(@PathVariable Integer id, @RequestBody EventDTO event) {
        EventEntity eventEntity = eventMapper.map(event);
        return eventService.updateEventById(eventEntity, id).map(eventMapper::map);
    }

    @DeleteMapping("/{id}")
    public Mono<EventDTO> deleteEventById(@PathVariable Integer id) {
        return eventService.deleteEventById(id).map(eventMapper::map);
    }


}
