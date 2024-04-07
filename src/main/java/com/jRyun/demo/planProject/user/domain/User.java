package com.jRyun.demo.planProject.user.domain;

import lombok.Data;

import java.io.Serializable;


@Data
public class User implements Serializable {

    private String id;
    private String pw;

    private String name;

    private String salt;

    public User(){}
    public User(String id, String pw, String name, String salt) {
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.salt = salt;
    }
}
