package com.Maxim.File_storage_API.service;


import com.Maxim.File_storage_API.entity.EventEntity;
import com.Maxim.File_storage_API.entity.Status;
import com.Maxim.File_storage_API.repository.EventRepository;
import com.Maxim.File_storage_API.repository.FileRepository;
import com.Maxim.File_storage_API.repository.UserRepository;
import io.r2dbc.spi.R2dbcDataIntegrityViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.r2dbc.core.DatabaseClient;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private  R2dbcEntityTemplate template;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private DatabaseClient databaseClient;

    public EventService() {
    }



    public Mono<EventEntity> getEventById(Integer eventId) {
        return eventRepository.findById(eventId);
    }

    public Flux<EventEntity> getAllEvents() {
        return eventRepository.findAll();
    }

    public Mono<EventEntity> saveEvents2(EventEntity event)  {

        return eventRepository.save(event);
    }


    public Mono<EventEntity> saveEvents(EventEntity event) throws R2dbcDataIntegrityViolationException{
        Mono<Boolean> userExists = userRepository.existsById(event.getUserId());
        Mono<Boolean> fileExists = fileRepository.existsById(event.getFileId());

        return Mono.zip(userExists, fileExists)
                .flatMap(tuple -> {
                    if (!tuple.getT1() && !tuple.getT2()) {
                       throw  new R2dbcDataIntegrityViolationException("User with id " + event.getUserId() + " and File with id " + event.getFileId() + " not found");
                    } else if (!tuple.getT1()) {
                        return Mono.error(new R2dbcDataIntegrityViolationException("User with id " + event.getUserId() + " not found"));
                    } else if (!tuple.getT2()) {
                        return Mono.error(new R2dbcDataIntegrityViolationException("File with id " + event.getFileId() + " not found"));
                    } else {
                        return eventRepository.save(event);
                    }
                })
                .switchIfEmpty(Mono.error(new R2dbcDataIntegrityViolationException("Bad Request")));
    }





    public Mono<EventEntity> updateEventById(EventEntity event) {
//        TODO когда вынесу в сервис в нем сделать проверку есть ли такой event_id
        return eventRepository.updateEventByIdAllColumns(event.getId(),event.getUser().getId(),event.getFile().getId(),event.getStatus())
                .then(getEventById(event.getId()));
    }

    public Mono<EventEntity> deleteEventById(Integer id) {
        return eventRepository.updateEventStatus(id, Status.DELETED);

    }






}
