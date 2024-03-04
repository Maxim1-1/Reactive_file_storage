package com.Maxim.File_storage_API.exceptions.security_exeptions;

import com.Maxim.File_storage_API.dto.UserDTO;

public class RegistrationError extends RuntimeException {
    private String message = "enter all required parameters: password, name, role";

    public RegistrationError() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
