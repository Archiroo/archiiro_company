package com.archiiro.app.Core.Service;

import com.archiiro.app.Core.Dto.UserDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    List<UserDto> getAllDto();

    UserDto getUserByUsername(String username);

    UserDto createDto(Long id, UserDto dto);

    UserDto updateDto(Long id, UserDto dto);

    UserDto deleteDto(Long id);
}
