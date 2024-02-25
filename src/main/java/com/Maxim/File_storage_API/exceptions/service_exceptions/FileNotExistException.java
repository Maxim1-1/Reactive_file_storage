package com.Maxim.File_storage_API.exceptions.service_exceptions;

public class FileNotExistException extends NotExistException {

    private final String messageTemplate = "File with id %d not exist";

    public FileNotExistException(Integer id) {
        super();
        this.setMessage(String.format(messageTemplate, id));
        this.setId(id);
    }

}
