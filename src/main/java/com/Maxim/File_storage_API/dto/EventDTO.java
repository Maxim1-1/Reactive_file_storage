package com.Maxim.File_storage_API.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EventDTO {

    private Integer id;
    @JsonBackReference
    private UserDTO userDTO;
    private FileDTO file;
    private String status;
    public Integer getId() {
        return id;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(Integer id) {this.id = id;}

//    public UserDTO getUserDTO() {
//        return userDTO;
//    }
//
//    public void setUserDTO(UserDTO userDTO) {
//        this.userDTO = userDTO;
//    }

    public FileDTO getFile() {
        return file;
    }

    public void setFile(FileDTO file) {
        this.file = file;
    }

}
