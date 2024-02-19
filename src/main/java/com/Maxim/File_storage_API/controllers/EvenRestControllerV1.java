package com.Maxim.File_storage_API.controllers;

import com.Maxim.File_storage_API.entity.EventEntity;
import com.Maxim.File_storage_API.entity.FileEntity;
import com.Maxim.File_storage_API.entity.Status;
import com.Maxim.File_storage_API.entity.UserEntity;
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
@RequestMapping("/events")
public class EvenRestControllerV1 {

    @Autowired
    private EventService eventService;


    @GetMapping("/{id}")
    public Mono<EventEntity> getEventById(@PathVariable Integer id) {
        return eventService.getEventById(id);
    }

    @GetMapping("")
    public Flux<EventEntity> getAllEvents() {
        return eventService.getAllEvents();
    }

    @PostMapping("")
    public Mono<EventEntity> saveEvents (@RequestBody EventEntity event) {
//        UserEntity user = new UserEntity();
//        user.setId(1);
//        FileEntity file = new FileEntity();
//        file.setId(1);
//        EventEntity event1 = new EventEntity();
//        event1.setFile(file);
//        event1.setStatus(Status.ACTIVE);
//        event1.setUser(user);
//
        return eventService.saveEvents(event);
    }

    @PutMapping("/{id}")
    public Mono<EventEntity> updateEventById(@PathVariable Integer id,@RequestBody EventEntity event) {
//        event.setId(id);

//        UserEntity user = new UserEntity();
//        user.setId(1);
//        FileEntity file = new FileEntity();
//        file.setId(1);
//        EventEntity event1 = new EventEntity();
//        event1.setId(id);
//        event1.setFile(file);
//        event1.setStatus(Status.ACTIVE);
//        event1.setUser(user);

        return eventService.updateEventById(event);
    }

    @DeleteMapping("/{id}")
    public Mono<EventEntity> deleteEventById(@PathVariable Integer id) {
        return eventService.deleteEventById(id);
    }
}
