package com.bloodlink.api.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "notifications")
public class Notification {
    
    @Id
    private String id;
    
    @DBRef
    private User sender;
    
    @DBRef
    private User receiver;
    
    private String title;
    private String message;
    
    private Boolean isRead = false;
    
    private Date createdAt;
    
    public Notification() {
        this.createdAt = new Date();
    }
}
