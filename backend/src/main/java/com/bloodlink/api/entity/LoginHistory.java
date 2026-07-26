package com.bloodlink.api.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "login_history")
public class LoginHistory {
    
    @Id
    private String id;
    
    @DBRef
    private User user;
    
    private Date loginTime;
    private Date logoutTime;
    private String device;
}
