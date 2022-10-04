package com.archiiro.app.Core.Dto;

import com.archiiro.app.Core.Domain.User;
import com.archiiro.app.Core.Domain.RoleUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserDto {
    private Long id;
    private LocalDateTime dateCreate;
    private String username;
    private String password;
    private Integer active;

    private List<RoleDto> roles;

    public UserDto() {

    }

    public UserDto(User entity) {
        this.id = entity.getId();
        this.dateCreate = entity.getDateCreate();
        this.username = entity.getUsername();
        this.password = entity.getPassword();
        this.active = entity.getActive();
        // Map bảng trung gian
        if(entity.getUserRoles() != null && !entity.getUserRoles().isEmpty()) {
            this.roles = new ArrayList<>();
            for(RoleUser roleUser : entity.getUserRoles()) {
                this.roles.add(new RoleDto(roleUser.getRole()));
            }
        }
    }

    public UserDto(User entity, boolean arc) {
        this.id = entity.getId();
        this.dateCreate = entity.getDateCreate();
        this.username = entity.getUsername();
        this.password = entity.getPassword();
        this.active = entity.getActive();
        // Map bảng trung gian
        if(entity.getUserRoles() != null && !entity.getUserRoles().isEmpty()) {
            this.roles = new ArrayList<>();
            for(RoleUser roleUser : entity.getUserRoles()) {
                this.roles.add(new RoleDto(roleUser.getRole()));
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDateTime dateCreate) {
        this.dateCreate = dateCreate;
    }

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

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public List<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDto> roles) {
        this.roles = roles;
    }
}
