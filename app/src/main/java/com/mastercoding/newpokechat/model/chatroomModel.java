package com.mastercoding.newpokechat.model;


import com.google.firebase.Timestamp;

import java.util.List;

public class chatroomModel {
    String chatroomId;
    List<String> userids;
    Timestamp lastmessageTimestamp;
    String lastMessageSenderid,lastMessage;


    public chatroomModel() {
    }

    public chatroomModel(String chatroomId, List<String> userids, Timestamp lastmessageTimestamp, String lastMessageSenderid) {
        this.chatroomId = chatroomId;
        this.userids = userids;
        this.lastmessageTimestamp = lastmessageTimestamp;
        this.lastMessageSenderid = lastMessageSenderid;
    }

    public String getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(String chatroomId) {
        this.chatroomId = chatroomId;
    }

    public List<String> getUserids() {
        return userids;
    }

    public void setUserids(List<String> userids) {
        this.userids = userids;
    }

    public Timestamp getLastmessageTimestamp() {
        return lastmessageTimestamp;
    }

    public void setLastmessageTimestamp(Timestamp lastmessageTimestamp) {
        this.lastmessageTimestamp = lastmessageTimestamp;
    }

    public String getLastMessageSenderid() {
        return lastMessageSenderid;
    }

    public void setLastMessageSenderid(String lastMessageSenderid) {
        this.lastMessageSenderid = lastMessageSenderid;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
