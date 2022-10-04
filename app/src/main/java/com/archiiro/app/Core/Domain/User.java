package com.archiiro.app.Core.Domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name="tbl_user")
@XmlRootElement
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="date_create")
    private LocalDateTime dateCreate;

    @Column(name="user_name")
    private String username;

    @Column(name="password")
    private String password;


    @Column(name="active")
    private Integer active; // 0: Chưa kích hoạt, 1: Đã kích hoạt

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoleUser> roleUsers;


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

    public Set<RoleUser> getUserRoles() {
        return roleUsers;
    }

    public void setUserRoles(Set<RoleUser> roleUsers) {
        this.roleUsers = roleUsers;
    }
}
