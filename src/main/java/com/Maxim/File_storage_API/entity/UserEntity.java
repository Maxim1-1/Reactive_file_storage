package com.Maxim.File_storage_API.entity;



import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;


@Table(name = "user")
public class UserEntity {

    @Id
    private Integer id;

    private String name;
    private String password;
    private Role role;
    private Status status;
    @Transient
    private  List<EventEntity> events;
    public UserEntity() {

    }

    public UserEntity(Integer id, String name, String password, Role role, Status status, List<EventEntity> events) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
        this.status = status;
        this.events = events;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EventEntity> getEvents() {
        return events;
    }

    public void setEvents(List<EventEntity> events) {
        this.events = events;
    }
}
