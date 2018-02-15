package com.nc.entity;

import java.util.List;

public class UserEntity {

    private Integer id;
    private String firstName;
    private String lastName;
    private Integer userGroupId;

    public UserEntity() {
    }

    public UserEntity(Integer id, String firstName, String lastName, Integer userGroupId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userGroupId = userGroupId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
    }
}
