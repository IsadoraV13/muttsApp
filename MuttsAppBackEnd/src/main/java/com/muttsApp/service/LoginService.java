package com.muttsApp.service;

import com.muttsApp.POJOs.User;
import com.muttsApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


//the service uses methods from the repo interface
@Service
public class LoginService {

    @Autowired
    private UserRepository repo;

    public List<User> listAllUsers() {

        return repo.findAll();
    }

    public void saveUser(User user) {
        repo.save(user);
    }

    public User findUserByEmail(String email) {
        return repo.findByEmail(email);
    }

//    public User findUserByUserId(int UserId) {
//        return repo.findByUserId(UserId);
//    }
}
