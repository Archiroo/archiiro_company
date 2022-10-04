package com.archiiro.app.Core.Repository;

import com.archiiro.app.Core.Domain.User;
import com.archiiro.app.Core.Dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("Select new com.archiiro.app.Core.Dto.UserDto(entity, true) From User entity Where (1=1)")
    List<UserDto> getAllDto();
    @Query("Select new com.archiiro.app.Core.Dto.UserDto(entity, true) From User entity Where (1=1)")
    Page<UserDto> getPageDto(Pageable pageable);

    @Query("Select entity From User entity Where entity.id = ?1")
    User getEntity(Long id);

    @Query("Select count(entity.id) From User entity Where entity.username = ?1")
    Long checkUsername(String username);

}
