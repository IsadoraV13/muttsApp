package com.muttsApp.service;

import com.muttsApp.POJOs.Role;
import com.muttsApp.POJOs.User;
import com.muttsApp.repositories.RoleRepository;
import com.muttsApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


//the service uses methods from the repo interface
@Service
public class UserService {

    @Autowired
    private UserRepository repo;
    private RoleRepository roleRepo;

    // after we have contacts this would only be an admin service
    public List<User> listAllUsers() {
        return repo.findAll();
    }

    // this would be an admin service (outside of the login service)
    public void saveUser(User user) {
        repo.save(user);
    }

    public void saveRole(Role role) {
        roleRepo.save(role);
    }

    public User findUserById(int userId) {
        return repo.findOne(userId); //******why?
    }

}
