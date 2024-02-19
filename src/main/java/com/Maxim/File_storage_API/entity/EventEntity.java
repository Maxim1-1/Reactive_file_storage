package com.Maxim.File_storage_API.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "events")
public class EventEntity {

    @Id
    private Integer id;

    @Transient
    private UserEntity userEntity;

    @Transient
    private FileEntity file;

    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return userEntity;
    }

    public void setUser(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public FileEntity getFile() {
        return file;
    }

    public void setFile(FileEntity file) {
        this.file = file;
    }

}
