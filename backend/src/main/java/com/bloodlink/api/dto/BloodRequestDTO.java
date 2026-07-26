package com.bloodlink.api.dto;

import lombok.Data;
import java.util.Date;

@Data
public class BloodRequestDTO {
    private String id;
    private String patientId;
    private String patientName;
    private String bloodGroupRequired;
    private Integer unitsRequired;
    private String emergencyLevel;
    private String status;
    private String additionalNotes;
    private Date createdAt;
}
