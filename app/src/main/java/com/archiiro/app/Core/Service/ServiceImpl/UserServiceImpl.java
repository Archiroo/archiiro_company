package com.archiiro.app.Core.Service.ServiceImpl;

import com.archiiro.app.Core.Domain.Role;
import com.archiiro.app.Core.Domain.User;
import com.archiiro.app.Core.Domain.RoleUser;
import com.archiiro.app.Core.Dto.RoleDto;
import com.archiiro.app.Core.Dto.UserDto;
import com.archiiro.app.Core.Repository.RoleRepository;
import com.archiiro.app.Core.Repository.RoleUserRepository;
import com.archiiro.app.Core.Repository.UserRepository;
import com.archiiro.app.Core.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.*;

@Service @Transactional
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserRepository userRepos;

    @Autowired
    private RoleRepository roleRepos;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleUserRepository roleUserRepos;

    @PersistenceContext
    private EntityManager manager;
    @Override
    public List<UserDto> getAllDto() {
        // get All
        return this.userRepos.getAllDto();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto userDto = this.roleUserRepos.getUserByUsername(username);
        if(userDto == null) {
            System.out.println("User not found");
            throw new UsernameNotFoundException("User not found");
        } else {
            System.out.println("User is: " + username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if(userDto.getRoles() != null && userDto.getRoles().size() > 0) {
            for(RoleDto item : userDto.getRoles()) {
                authorities.add(new SimpleGrantedAuthority(item.getName()));
            }
        }
        return new org.springframework.security.core.userdetails.User(userDto.getUsername(), userDto.getPassword(), authorities);
    }

    @Override
    public UserDto getUserByUsername(String username) {
        if(username != null) {
            Long number = this.userRepos.checkUsername(username);
            if(number==1) {
                return this.roleUserRepos.getUserByUsername(username);
            }
            return null;
        }
        return null;
    }

    @Override
    public UserDto createDto(Long id, UserDto dto) {
        return this.updateDto(null, dto);
    }

    @Override
    public UserDto updateDto(Long id, UserDto dto) {
        User entity = null;
        LocalDateTime dateCreate = LocalDateTime.now();
        if(id != null) {
            Optional<User> userOptional = this.userRepos.findById(id);
            if(userOptional.isPresent()) {
                entity = userOptional.get();
            }
        } else if(dto.getId() != null) {
            Optional<User> userOptional = this.userRepos.findById(dto.getId());
            if(userOptional.isPresent()) {
                entity = userOptional.get();
            }
        }
        if(entity == null) {
            entity = new User();
            entity.setDateCreate(dateCreate);
        }
        if(dto.getUsername() != null) {
            entity.setUsername(dto.getUsername());
        }
//        if(dto.getPassword() != null) {
//            entity.setPassword(dto.getPassword());
//        }
        if(dto.getPassword() != null) {
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if(dto.getActive() != null) {
            entity.setActive(dto.getActive());
        }
        // Lưu đồng thời sang bảng User_Role
        if(dto.getRoles() != null && dto.getRoles().size() > 0) {
            HashSet<RoleUser> roleUserDtos = new HashSet<>();
            for(RoleDto roleDto : dto.getRoles()) {
                RoleUser roleUser = new RoleUser();
                Role role = null;
                if(roleDto != null) {
                    if(roleDto.getId() != null) {
                        Optional<Role> roleOptional = this.roleRepos.findById(roleDto.getId());
                        if(roleOptional.isPresent()) {
                            role = roleOptional.get();
                        }
                    }
                }
                roleUser.setUser(entity);
                if(role != null) {
                    roleUser.setRole(role);
                }
                roleUserDtos.add(roleUser);
            }
            // Trường hợp đã có dl
            if(entity.getUserRoles() != null) {
                entity.getUserRoles().clear();
                entity.getUserRoles().addAll(roleUserDtos);
            } else {
                entity.setUserRoles(roleUserDtos);
            }
        } else {
            entity.getUserRoles().clear();
        }
        entity = this.userRepos.save(entity);
        return new UserDto(entity, true);
    }

    @Override
    public UserDto deleteDto(Long id) {
        User entity = null;
        if(id != null) {
            Optional<User> userSerOptional = this.userRepos.findById(id);
            if (userSerOptional.isPresent()) {
                entity = userSerOptional.get();
                this.userRepos.delete(entity);
                return new UserDto(entity, true);
            }
        }
        return null;
    }
}
