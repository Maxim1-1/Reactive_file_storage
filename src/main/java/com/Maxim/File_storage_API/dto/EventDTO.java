package com.Maxim.File_storage_API.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EventDTO {

    private Integer id;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UserDTO user;

//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
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

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public FileDTO getFile() {
        return file;
    }

    public void setFile(FileDTO file) {
        this.file = file;
    }

}
