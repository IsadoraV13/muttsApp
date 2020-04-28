package com.muttsApp.controller;


import com.muttsApp.POJOs.Chat;
import com.muttsApp.POJOs.MessagePreview;
import com.muttsApp.POJOs.ResponseObject;
import com.muttsApp.POJOs.User;
import com.muttsApp.service.ChatService;
import com.muttsApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// the controller uses methods from the service class
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ChatService chatService;

    // at first (when we don't have contacts, every user can see this - but then only the admin should see this)
    @GetMapping
    public ResponseObject<List<User>> ViewAllUsers(Model model) {
        ResponseObject<List<User>> res = new ResponseObject();
        res.setData(userService.listAllUsers());
        return res;
    }

    @GetMapping(value="{userId}")
    public ResponseObject<User> ViewUser(@PathVariable(name = "userId") int userId){
            ResponseObject<User> res = new ResponseObject();
        res.setData(userService.findUserById(userId));
        return res;
    }

    @GetMapping("/{userId}/chats")
    // this should return all the chats for this user
    public ResponseObject<List<Chat>> viewUserChats(@PathVariable(name = "userId") int userId) {
        ResponseObject<List<Chat>> res = new ResponseObject();
        res.setData(chatService.listChatsbyUserId(userId));
        return res;
    }

    @GetMapping("/{userId}/msgpreviews")
    // this should return all the message previews for this user
    public ResponseObject<List<MessagePreview>> viewMsgPreviews(@PathVariable(name = "userId") int userId) {
        ResponseObject<List<MessagePreview>> res = new ResponseObject();
        res.setData(chatService.createMsgPreviews(userId));
        return res;
    }

    @GetMapping("/{userId}/chats/{chatId}")
    // this should return the specific chat (associated with this user). Doesn't return messages
    public ResponseObject<Chat> viewUserChats(@PathVariable(name = "userId") int userId, @PathVariable(name = "chatId") int chatId) {
        ResponseObject<Chat> res = new ResponseObject();
        res.setData(chatService.listByChatId(chatId));
        return res;
    }


}