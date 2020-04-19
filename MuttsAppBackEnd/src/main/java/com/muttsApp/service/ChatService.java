package com.muttsApp.service;

import com.muttsApp.POJOs.Chat;
import com.muttsApp.repositories.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


//the service uses methods from the repo interface
@Service
public class ChatService {

    @Autowired
    private ChatRepository repo;

    // after we have contacts this would only be an admin service
    public List<Chat> listAllChatsbyUserId(int senderId) {
        return repo.findByUserId(senderId);
    }

    public void saveChat(Chat chat) {
        repo.save(chat); //***********why?
    }

    public Chat findByChatId(int chatId) {
        return repo.findByChatId(chatId);
    }

    public List<Chat> getChatByUserId(int userId) {
        return repo.findByUserId(userId);
    }

}
