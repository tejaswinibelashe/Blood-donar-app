package com.bloodlink.api.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "patients")
public class Patient {
    
    @Id
    private String id;
    
    @DBRef
    private User user;
    
    private String hospitalName;
    private String emergencyLevel;
}
