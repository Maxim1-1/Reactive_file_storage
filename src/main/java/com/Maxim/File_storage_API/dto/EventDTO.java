package com.Maxim.File_storage_API.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;


public class EventDTO {

    private Integer id;

    @JsonBackReference
    private UserDTO userDTO;
    private FileDTO fileDTO;

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

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public FileDTO getFileDTO() {
        return fileDTO;
    }

    public void setFileDTO(FileDTO fileDTO) {
        this.fileDTO = fileDTO;
    }

}
