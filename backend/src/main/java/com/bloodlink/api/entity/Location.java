package com.bloodlink.api.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "locations")
public class Location {
    
    @Id
    private String id;
    
    @DBRef
    private User user;
    
    private Double latitude;
    private Double longitude;
    
    private Date updatedAt;
    
    public Location() {
        this.updatedAt = new Date();
    }
}
