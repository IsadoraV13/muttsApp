package com.muttsApp.controller;

import com.muttsApp.POJOs.Message;
import com.muttsApp.POJOs.ResponseObject;
import com.muttsApp.service.ChatService;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/{chatId}")
    // this should return all the messages in this chat
    public ResponseObject<List<Message>> viewAllMessagesByChat(@PathVariable(name = "chatId") int chatId) {
        ResponseObject<List<Message>> res = new ResponseObject();
        res.setData(chatService.listAllMsgsByChat(chatId));
        return res;
    }

    @GetMapping("/{chatId}/last")
    // this should return the last message in this chat
    public ResponseObject<Message> viewLastMessage(@PathVariable(name = "chatId") int chatId) {
        ResponseObject<Message> res = new ResponseObject();
        res.setData(chatService.listLastMsg(chatId));
        return res;
    }

    @PostMapping("/{chatId}/message")
    public ResponseObject<Message> createMessage(@RequestBody Message msg) {
        ResponseObject<Message> res = new ResponseObject();
        res.setData(chatService.saveMessage(msg));
        return res;
    }

    @DeleteMapping("/{chatId}")
    public void deleteChat(@PathVariable(value="chatId")int chatId) throws ObjectNotFoundException {
        chatService.deleteChat(chatId);
    }

    @DeleteMapping("/{messageId}")
    public void deleteMessage(@PathVariable(value="messageId")int messageId) throws ObjectNotFoundException {
        chatService.deleteChat(messageId);
    }
}
