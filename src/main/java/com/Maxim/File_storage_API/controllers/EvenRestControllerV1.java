package com.Maxim.File_storage_API.controllers;

import com.Maxim.File_storage_API.dto.EventDTO;
import com.Maxim.File_storage_API.entity.EventEntity;
import com.Maxim.File_storage_API.entity.FileEntity;
import com.Maxim.File_storage_API.entity.Status;
import com.Maxim.File_storage_API.entity.UserEntity;
import com.Maxim.File_storage_API.mapper.EventMapper;
import com.Maxim.File_storage_API.mapper.FileMapper;
import com.Maxim.File_storage_API.repository.EventRepository;
import com.Maxim.File_storage_API.service.EventService;
import com.Maxim.File_storage_API.service.FileService;
import com.Maxim.File_storage_API.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

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
    public Mono<EventEntity> getEventById(@PathVariable Integer id) {
        return eventService.getEventById(id);
    }

    @GetMapping("")
    public Flux<EventDTO> getAllEvents() {
        return eventService.getAllEvents().map(eventMapper::map);
    }

    @PostMapping("")
    public Mono<EventDTO> saveEvents (@RequestBody EventDTO event) {
          EventEntity eventEntity = eventMapper.map(event);
          return eventService.saveEvent(eventEntity).map(eventMapper::map);
    }

    @PutMapping("/{id}")
    public Mono<EventDTO> updateEventById(@PathVariable Integer id,@RequestBody EventDTO event) {
        event.setId(id);
        EventEntity eventEntity = eventMapper.map(event);
        return eventService.updateEventById(eventEntity).map(eventMapper::map);
    }

    @DeleteMapping("/{id}")
    public Mono<EventEntity> deleteEventById(@PathVariable Integer id) {
        return eventService.deleteEventById(id);
    }
}
