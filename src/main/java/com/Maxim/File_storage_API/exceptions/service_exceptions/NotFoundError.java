package com.Maxim.File_storage_API.exceptions.service_exceptions;

public class NotFoundError {

    private String message;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotFoundError() {
    }

    public NotFoundError(String message) {
        this.message = message;
    }
}
