package com.muttsApp.controller;


import com.muttsApp.POJOs.User;
import com.muttsApp.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

// the controller uses methods from the service class
@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
    public String login(Model model){
        return "login";
    }

    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public String registration(Model model){
        User user = new User();
        model.addAttribute("user", user); //we do model.addAttribute(user) here to pass the user attributes
        // which are then displayed on the registration view
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String createNewUser(@Valid User user, BindingResult bindingResult, Model model) {

        User userExists = loginService.findUserByEmail(user.getEmail());

        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }

        if (bindingResult.hasErrors()) {
            return "registration";

        } else {
            loginService.saveUser(user);
            model.addAttribute("successMessage", "User has been registered successfully");
            model.addAttribute("user", new User());
            return "registration";
        }

    }
    @RequestMapping(value="/home", method = RequestMethod.GET)
    public String index(Authentication auth, Model model){
        int user_id = loginService.findUserByEmail(auth.getName()).getUserId(); //check this
        model.addAttribute("user_id", user_id);
        return "user";
    }
}