package com.Maxim.File_storage_API.exceptions.security_exeptions;

public class AuthenticationError {
    private String message;

    public AuthenticationError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
