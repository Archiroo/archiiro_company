package com.archiiro.app.Core.Dto;
import com.archiiro.app.Core.Domain.RoleUser;

public class RoleUserDto {
    private UserDto userDto;
    private RoleDto roleDto;

    public RoleUserDto() {

    }

    public RoleUserDto(RoleUser entity) {
        if(entity.getUser() != null) {
            this.userDto = new UserDto(entity.getUser());
        }
        if(entity.getRole() != null) {
            this.roleDto = new RoleDto(entity.getRole());
        }
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public RoleDto getRoleDto() {
        return roleDto;
    }

    public void setRoleDto(RoleDto roleDto) {
        this.roleDto = roleDto;
    }
}
