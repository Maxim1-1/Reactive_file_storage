package com.Maxim.File_storage_API.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;


public class UserDTO {

    private Integer id;
    private String name;
    @JsonManagedReference
    private  List<EventDTO> eventsDTO;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EventDTO> getEventsDTO() {
        return eventsDTO;
    }

    public void setEventsDTO(List<EventDTO> eventDTOS) {
        this.eventsDTO = eventDTOS;
    }
}
