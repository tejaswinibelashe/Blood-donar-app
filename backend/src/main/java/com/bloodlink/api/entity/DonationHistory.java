package com.bloodlink.api.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "donation_history")
public class DonationHistory {
    
    @Id
    private String id;
    
    @DBRef
    private User donor;
    
    @DBRef
    private User patient;
    
    @DBRef
    private Hospital hospital;
    
    private Date donationDate;
    private String bloodGroup;
    private Integer units;
}
