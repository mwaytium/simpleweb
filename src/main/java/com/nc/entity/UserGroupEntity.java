package com.nc.entity;

import java.util.ArrayList;
import java.util.List;

public class UserGroupEntity {

    private Integer id;
    private String name;
    private List<UserEntity> users = new ArrayList<>();

    public UserGroupEntity() {
    }

    public UserGroupEntity(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserGroupEntity(Integer id, String name, UserEntity user) {
        this.id = id;
        this.name = name;
        this.users.add(user);
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

    public List<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }
}
