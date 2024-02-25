package com.Maxim.File_storage_API.exceptions.service_exceptions;

public class EventNotExistException extends NotExistException {

    private final String messageTemplate = "Event with id %d not exist";

    public EventNotExistException(Integer id) {
        super();
        this.setMessage(String.format(messageTemplate, id));
        this.setId(id);
    }

}
