package com.muttsApp.service;

import com.muttsApp.POJOs.Chat;
import com.muttsApp.POJOs.Message;
import com.muttsApp.POJOs.MessagePreview;
import com.muttsApp.POJOs.User;
import com.muttsApp.repositories.ChatRepository;
import com.muttsApp.repositories.MessageRepository;
import com.muttsApp.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


//the service uses methods from the userRepo interface
@Service
public class ChatService {

    private static Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private ChatRepository chatRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private MessageRepository msgRepo;
    @Autowired
    private UserService userService;

//    @Cacheable(value = "allChats")
    public List<Chat> listAllChats() {
//        LOG.info("return all chats");
        return chatRepo.findAll();
    }

    //    @CachePut(key = "#newMsg")
    public Message saveMessage(Message newMsg) {
//        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
//        newMsg.setTimestamp(currentTimestamp);
        return msgRepo.save(newMsg);
    }

    //    @CachePut(key = "#newChat")
    public Chat saveChat(Chat newChat, int userId, int duoUserId) {
        User user = userRepo.getOne(userId);
        User duoUser = userRepo.getOne(duoUserId);
        user.setUserchat(new HashSet<Chat>(Arrays.asList(newChat)));
        duoUser.setUserchat(new HashSet<Chat>(Arrays.asList(newChat)));
        return chatRepo.save(newChat);
    }

    // until we have contacts this is used to show all the users to someone who wants to start a new chat
    public List<Chat> listChatsbyUserId(int userId) {
    // userId of logged in user is already available as a parameter in the URL (via the hidden html element)
        User user = userService.findUserById(userId);
        return new ArrayList<Chat>(user.getUserchat());
    }

    public List<Chat> listGroupChats(int userId) {
        List<Chat> chats = listChatsbyUserId(userId);
        List<Chat> groupChats = new ArrayList<>();
        for (Chat chat: chats) {
            if (chatRepo.findUserIdsinChat(chat.getChatId()).size() > 2)
                groupChats.add(chat);
        }
        return groupChats;
    }

    public List<Chat> listDuoChats(int userId) {
        List<Chat> chats = listChatsbyUserId(userId);
        List<Chat> indvChats = new ArrayList<>();
        for (Chat chat: chats) {
            if (chatRepo.findUserIdsinChat(chat.getChatId()).size() <= 2)
                indvChats.add(chat);
        }
        return indvChats;
    }

    public List<Integer> listUsersIdsInDuoChat(int userId) {
        List<Chat> chats = listDuoChats(userId);
        int otherUserId;
        List<Integer> OtherUserIds = new ArrayList<>();
        for (Chat chat: chats) {
            //remove the user from the list
            ArrayList<Integer> arr = new ArrayList<>(chatRepo.findUserIdsinChat(chat.getChatId()));
            arr.remove(Integer.valueOf(userId));
            OtherUserIds.add(arr.get(0));
        }
        return OtherUserIds;
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


    public void deleteChat(int chatId) {
        chatRepo.deleteByChatId(chatId);
    }

//    @Cacheable(value = "allChats", key = "userId")
    public List<MessagePreview> createMsgPreviews(int userId) {
        //        LOG.info("return all msg previews");
        //find all the user's chats
        List<Chat> groupChats = listGroupChats(userId);
        List<Chat> duoChats = listDuoChats(userId);
        List<MessagePreview> msgPreviews = new ArrayList<>();
        Message lastMsg;

        if (groupChats.size() > 0) {
            for (int i = 0; i < (groupChats.size()); i++) {
                MessagePreview mp = new MessagePreview();
                mp.setChatPhotoUrl(groupChats.get(i).getChatPhotoUrl());
                mp.setChatName(groupChats.get(i).getChatName());
                mp.setChatId(groupChats.get(i).getChatId());
                lastMsg = listLastMsg(groupChats.get(i).getChatId());
                mp.setLastMsgContent(lastMsg.getContent());
                mp.setLastMsgtimestamp(lastMsg.getTimestamp());
                msgPreviews.add(mp);
            }
        }
        if (duoChats.size() > 0) {
            for (int j = 0; j < (duoChats.size()); j++) {
                if (null != listLastMsg(duoChats.get(j).getChatId())) {
                    MessagePreview mp = new MessagePreview();
                    User sender = userService.findUserById(listUsersIdsInDuoChat(userId).get(j));
                    mp.setChatPhotoUrl(sender.getProfilePicUrl());
                    String senderName = userService.findUserNameById(listUsersIdsInDuoChat(userId).get(j));
                    mp.setChatName(senderName);
                    mp.setChatId(duoChats.get(j).getChatId());
                    lastMsg = listLastMsg(duoChats.get(j).getChatId());
                    mp.setLastMsgContent(lastMsg.getContent());
                    mp.setLastMsgtimestamp(lastMsg.getTimestamp());
                    msgPreviews.add(mp);
                }
            }
        }
        System.out.println(msgPreviews.toString());
        return msgPreviews;
    }


}
