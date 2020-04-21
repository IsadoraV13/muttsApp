package com.muttsApp.POJOs;


import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="user")
//@SecondaryTable(name = "userrole", pkJoinColumns = @PrimaryKeyJoinColumn(name = "userId"))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;
    @NotEmpty(message = "Username may not be empty")
    private String userName;
    @Column(columnDefinition = "boolean default 1")
    Boolean enabled;
    @NotEmpty(message = "Password may not be blank")
    private String password;
    @NotEmpty(message = "Email may not be null")
    private String email;
    private String firstName;
    private String lastName;
    private String profilePicUrl;
//    @Column(name = "roleId", table = "userrole", columnDefinition = "int default 1")
//    private Integer roleId;
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "userrole",
            joinColumns = { @JoinColumn(name = "userId") },
            inverseJoinColumns = { @JoinColumn(name = "roleId") }
    )
    Set<Role> userrole = new HashSet<>();


    public User() {
    }

    public User(String userName, String password, String email) {
        this.userName = userName;
        this.password = password;
        this.email = email;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public Set<Role> getUserrole() {
        return userrole;
    }

    public void setUserrole(Set<Role> userrole) {
        this.userrole = userrole;
    }

//    public Integer getRoleId() {
//        return roleId;
//    }
//
//    public void setRoleId(Integer roleId) {
//        this.roleId = roleId;
//    }
}
