package com.muttsApp.POJOs;

import javax.persistence.*;


// a chat will be either a group chat or a 1on1 chat (for iteration1)
// there will be a table for messages and a message will be attached to a chatId
// the message will itself have attributes like content and timestamp

@Entity
@Table(name="chat")
public class Chat {
    private int chatId;
    private String chatName;
    private int userId;
    private int recipientId;
    private String chatPhotoUrl;
    private int adminId;
    private int lastMessageId; // this should contain the date/timestamp to order the chatPreviews chronologically

    public Chat() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }

    public String getChatPhotoUrl() {
        return chatPhotoUrl;
    }

    public void setChatPhotoUrl(String chatPhotoUrl) {
        this.chatPhotoUrl = chatPhotoUrl;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public int getLastMessageId() {
        return lastMessageId;
    }

    public void setLastMessageId(int lastMessageId) {
        this.lastMessageId = lastMessageId;
    }
}
