package com.muttsApp.POJOs;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int roleId;
    @NotNull
    private String role;

//    @ManyToMany(mappedBy = "userrole")
//    private Set<User> user = new HashSet<>();

    public Role() {
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

//    public Set<User> getUser() {
//        return user;
//    }
//
//    public void setUser(Set<User> users) {
//        this.user = users;
//    }
}
