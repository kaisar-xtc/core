package com.dksys.core.entity;

import java.io.Serializable;

/**
 * @version 1.0.0
 * @Author Wang
 * @createTime 2021-08-06 11:26:00
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
