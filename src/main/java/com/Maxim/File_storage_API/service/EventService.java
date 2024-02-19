package com.Maxim.File_storage_API.service;


import com.Maxim.File_storage_API.entity.EventEntity;
import com.Maxim.File_storage_API.repository.EventRepository;
import com.Maxim.File_storage_API.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import org.springframework.r2dbc.core.DatabaseClient;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private DatabaseClient databaseClient;


    public Mono<EventEntity> getEventByIdWithFile(Integer eventId) {
        return databaseClient.sql("SELECT file_id FROM rest.events where id = :eventId")
                .bind("eventId", eventId)
                .map(row -> row.get("file_id", Integer.class))
                .one()
                .flatMap(fileId -> fileRepository.findById(fileId)
                        .flatMap(file -> eventRepository.findById(eventId)
                                .map(event -> {
                                    event.setFile(file);
                                    return event;
                                })
                        )
                );
    }

//    public Event saveEvent(Event event) {
//        return eventRepository.save(event);
//    }
//
//    public Event getEventById(Integer eventId) {
//        Optional<Event> optionalEvent = eventRepository.findById(eventId);
////        TODO добавить обработку на предмет null
//        return optionalEvent.get();
//    }
//
//    public List<Event> getAllEvents() {
//        return eventRepository.findAll();
//    }
//
//    public Event updateEventById(Event event) {
//
////TODO make it through, userRepository.existsById(id)
//// +  сделать сравнение полей юзера из бд, и полей из json
//        //        TODO добавить обработку на предмет null
//        return null;
//    }
//
//    public void deleteEventById(Integer id) {
//        //TODO change status instead of delete
//        //        TODO добавить обработку на предмет null
////        userRepository.existsById(id)
//        eventRepository.deleteById(id);
//    }
}
