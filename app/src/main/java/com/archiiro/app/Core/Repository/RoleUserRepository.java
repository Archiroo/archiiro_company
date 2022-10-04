package com.archiiro.app.Core.Repository;

import com.archiiro.app.Core.Domain.RoleUser;
import com.archiiro.app.Core.Dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleUserRepository extends JpaRepository<RoleUser, Long> {
    @Query("Select new com.archiiro.app.Core.Dto.UserDto(entity.user, true) From RoleUser entity Where entity.user.username = ?1")
    UserDto getUserByUsername(String username);
}
