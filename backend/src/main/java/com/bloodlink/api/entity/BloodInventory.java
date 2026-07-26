package com.bloodlink.api.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "blood_inventory")
public class BloodInventory {
    
    @Id
    private String id;
    
    @DBRef
    private Hospital hospital;
    
    private String bloodGroup;
    private Integer availableUnits;
}
