package com.Maxim.File_storage_API.security;

import java.security.Principal;

public class CustomPrincipal implements Principal {

    public CustomPrincipal(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

