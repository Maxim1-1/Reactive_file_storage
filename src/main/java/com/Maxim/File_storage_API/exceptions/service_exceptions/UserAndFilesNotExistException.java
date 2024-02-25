package com.Maxim.File_storage_API.exceptions.service_exceptions;

public class UserAndFilesNotExistException extends NotFoundException {

    private final String messageTemplate = "User with id %d and file with id %d not exist";

    public UserAndFilesNotExistException(Integer userId, Integer fileId) {
        super();
        this.setMessage(String.format(messageTemplate, userId,fileId));
    }

}
