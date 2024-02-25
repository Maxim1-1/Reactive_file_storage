package com.Maxim.File_storage_API.exceptions.service_exceptions;

public class UserNotExistException extends NotFoundException {

    private final String messageTemplate = "User with id %d not exist";

    public UserNotExistException(Integer id) {
        super();
        this.setMessage(String.format(messageTemplate, id));
        this.setId(id);
    }

}
