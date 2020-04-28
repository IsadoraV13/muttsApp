package com.muttsApp.POJOs;

public class MessagePreview {
    private int chatId;
    private String chatName;
    private String chatPhotoUrl;
    private String lastMsgContent;
    private String lastMsgtimestamp;

    public MessagePreview() {
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

    public String getLastMsgContent() {
        return lastMsgContent;
    }

    public void setLastMsgContent(String lastMsgContent) {
        this.lastMsgContent = lastMsgContent;
    }

    public String getLastMsgtimestamp() {
        return lastMsgtimestamp;
    }

    public void setLastMsgtimestamp(String lastMsgtimestamp) {
        this.lastMsgtimestamp = lastMsgtimestamp;
    }
}
