package com.Maxim.File_storage_API.exceptions.service_exceptions;

public abstract class NotExistException extends RuntimeException {
    private Integer id;
    private String message;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotExistException() {
    }

    public NotExistException(String message) {
        this.message = message;
    }
}