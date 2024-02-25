package com.Maxim.File_storage_API.exceptions.security_exeptions;

public class UserNotAuthenticatedException extends Throwable {

    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserNotAuthenticatedException(String message) {
        this.message=message;
    }


}
