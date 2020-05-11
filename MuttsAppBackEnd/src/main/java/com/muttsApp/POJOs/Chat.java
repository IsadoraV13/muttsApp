package com.muttsApp.POJOs;

import javax.persistence.*;


// a chat will be either a group chat or a 1on1 chat (for iteration1)
// there will be a table for messages and a chat will be attached every message
// the message will itself have attributes like content, timestamp and senderId

@Entity
@Table(name="chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int chatId; // a message will contain a chatId
    private String chatName;
// I have decided not to have users in the chat class (because a chat can technically have A LOT of users).
// There will be a joined table for chat/user.
// all users associated with a certain chatId are recipients except the sender
// the senderId is present in the message object
    private String chatPhotoUrl;


    public Chat() {
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getChatPhotoUrl() {
        return chatPhotoUrl;
    }

    public void setChatPhotoUrl(String chatPhotoUrl) {
        this.chatPhotoUrl = chatPhotoUrl;
    }

}
