package com.bloodlink.api.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "messages")
public class Message {
    
    @Id
    private String id;
    
    @DBRef
    private User sender;
    
    @DBRef
    private User receiver;
    
    @DBRef
    private BloodRequest bloodRequest;
    
    private String content;
    
    private Boolean isRead = false;
    
    private Date timestamp;
    
    public Message() {
        this.timestamp = new Date();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public User getSender() { return sender; }
    public void setSender(User sender) { this.sender = sender; }

    public User getReceiver() { return receiver; }
    public void setReceiver(User receiver) { this.receiver = receiver; }

    public BloodRequest getBloodRequest() { return bloodRequest; }
    public void setBloodRequest(BloodRequest bloodRequest) { this.bloodRequest = bloodRequest; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
}
