package com.Maxim.File_storage_API.exceptions.service_exceptions;

public class NotExistError {

    private String message;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotExistError() {
    }

    public NotExistError(String message) {
        this.message = message;
    }
}
