package com.muttsApp.service;

import com.muttsApp.POJOs.Chat;
import com.muttsApp.POJOs.Message;
import com.muttsApp.POJOs.MessagePreview;
import com.muttsApp.POJOs.User;
import com.muttsApp.repositories.ChatRepository;
import com.muttsApp.repositories.MessageRepository;
import com.muttsApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


//the service uses methods from the userRepo interface
@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private MessageRepository msgRepo;
    @Autowired
    private UserService userService;


    // until we have contacts this is used to show all the users to someone who wants to start a new chat
    public List<Chat> listChatsbyUserId(int userId) {
    // the userId of the user who is currently logged in is already available as a parameter in the URL (via the
        // hidden html element
        User user = userService.findUserById(userId);
        return new ArrayList<Chat>(user.getUserchat());
    }

    public Chat listByChatId(int chatId) {
        return chatRepo.findByChatId(chatId);
    }

    public Message listLastMsg(int chatId) {
//        return msgRepo.findByChatId(sortByMessageIdDesc());
        return msgRepo.findByLastMsg(chatId);
    }

//    private Sort sortByMessageIdDesc() {
//        return new Sort(Sort.Direction.DESC, "MessageId");
//    }

    public List<Message> listAllMsgsByChat(int chatId) {
        return msgRepo.findByChatId(chatId);
    }

    // assumption is you cannot create a chat without a new message
    public void saveChat(Chat chat, Authentication auth) {
        Message newMsg = new Message();
        User user = userRepo.findOne(userService.findUserByEmail(auth.getName()).getUserId());
        user.setUserchat(new HashSet<Chat>(Arrays.asList(chat)));
        msgRepo.save(newMsg);
        chatRepo.save(chat);
    }

    public Message saveMessage(Message newMsg) {
        return msgRepo.save(newMsg);
    }

    public void deleteChat(int chatId) {
        chatRepo.deleteByChatId(chatId);
    }

    public List<MessagePreview> createMsgPreviews(int userId) {
        //find all the user's chats
        List<Chat> chats = listChatsbyUserId(userId);
        Message lastMsg;
        //there will be as many previews as chats
        List<MessagePreview> msgPreviews = new ArrayList<>();
        for (int i = 0; i < chats.size(); i++) {
            MessagePreview mp = new MessagePreview();
            //for each preview, set the attributes based on each chat returned (and the lastMsg of each chat)
            mp.setChatId(chats.get(i).getChatId());
            mp.setChatPhotoUrl(chats.get(i).getChatPhotoUrl());
            lastMsg = listLastMsg(chats.get(i).getChatId());
            mp.setChatName(userService.findUserNameById(lastMsg.getSenderId())); //needs to change
            mp.setLastMsgContent(lastMsg.getContent());
            mp.setLastMsgtimestamp(lastMsg.getTimestamp());
            msgPreviews.add(mp);
        }
        return msgPreviews;
    }


}
