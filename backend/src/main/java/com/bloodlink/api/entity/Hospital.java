package com.bloodlink.api.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "hospitals")
public class Hospital {
    
    @Id
    private String id;
    
    private String hospitalName;
    private String address;
    private String phone;
    private String email;
    
    private Double latitude;
    private Double longitude;
    
    private String openingHours;
    private Integer availableUnits;
}
