package com.muttsApp.service;

import com.muttsApp.POJOs.Role;
import com.muttsApp.POJOs.User;
import com.muttsApp.repositories.RoleRepository;
import com.muttsApp.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


//the service uses methods from the userRepo interface
@Service
public class UserService {

    private static Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RoleRepository roleRepo;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    // after we have contacts this would only be an admin service
//    @Cacheable(value = "allUsers")
    public List<User> listAllUsers() {
//        LOG.info("return all chats");
        return userRepo.findAll();
    }

//    @CachePut(value = "allUsers", key = "#user")
    public void saveUser(User user) {
//        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        Role userRole = roleRepo.findByRole("USER");
        user.setUserrole(new HashSet<Role>(Arrays.asList(userRole)));
        userRepo.save(user);
    }

    public User findUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public User findUserById(int userId) {
        return userRepo.findOne(userId);
    }

    public String findUserNameById(int userId) {
        return userRepo.findOne(userId).getFirstName();
    }

}
