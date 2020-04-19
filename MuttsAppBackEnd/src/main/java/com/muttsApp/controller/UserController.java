package com.muttsApp.controller;


import com.muttsApp.POJOs.User;
import com.muttsApp.service.ChatService;
import com.muttsApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

// the controller uses methods from the service class
@Controller
public class UserController {

    @Autowired
    private UserService userService;
    private ChatService chatService;

    // at first (when we don't have contacts, every user will see this - but then only the admin should see this)
    @RequestMapping(value={"/user"}, method = RequestMethod.GET)
    public String login(Model model){
        List<User> listUsers = userService.listAllUsers();
        model.addAttribute("listUsers", listUsers);
        return "user";
    }

//    @RequestMapping(value={"/{UserId}"}, method = RequestMethod.GET)
//    public String ViewUserPage(@PathVariable(name = "UserId") int userId, Model model){
//        User user = userService.findUserById(userId);
//        model.addAttribute("user", user);
//        return "user";
//    }

//    @RequestMapping("/{UserId}/chats")
//    // this should return all the chats for this user, as well as the message previews
//    public String viewUserChatPage(@PathVariable(name = "UserId") int userId, Model model) {
//        // need a for each loop here?
//        List<Chat> chat = chatService.getChatByUserId(userId);
//
//        // need another for each loop here?
//        MessagePreview messagePreview = messageService.getMessagebyChatId();
//        model.addAttribute("messagePreview", messagePreview);
//
//        return "redirect:/user"; //double check thinking here
//    }
//
//    @RequestMapping(value="/home", method = RequestMethod.GET)
//    public String index(Authentication auth, Model model){
//        int user_id = userService.findUserByEmail(auth.getName()).getUserId(); //check this
//        model.addAttribute("user_id", user_id);
//        return "user";
//    }
}