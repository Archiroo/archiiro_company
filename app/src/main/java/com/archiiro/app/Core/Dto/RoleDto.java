package com.archiiro.app.Core.Dto;

import com.archiiro.app.Core.Domain.Role;

public class RoleDto {
    private Long id;
    private String name;

    public RoleDto() {

    }

    public RoleDto(Role entity) {
        this.id = entity.getId();
        this.name = entity.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
