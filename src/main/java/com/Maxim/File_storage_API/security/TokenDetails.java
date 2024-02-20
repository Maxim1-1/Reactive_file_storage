package com.Maxim.File_storage_API.security;

import java.util.Date;

public class TokenDetails {

    private Integer userId;
    private String token;
    private Date issuedAt;
    private Date expiresAt;

    public TokenDetails(Integer userId, String token, Date issuedAt, Date expiresAt) {
        this.userId = userId;
        this.token = token;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
    }

    public TokenDetails() {

    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Date issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }
}


