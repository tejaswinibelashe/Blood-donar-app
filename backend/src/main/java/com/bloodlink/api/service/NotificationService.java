package com.bloodlink.api.service;

import com.bloodlink.api.entity.BloodRequest;
import com.bloodlink.api.entity.User;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    /**
     * Stubs out the FCM (Firebase Cloud Messaging) logic.
     * In production, this uses the Firebase Admin SDK to push payloads to Android devices.
     */
    public void sendNotification(User recipient, String title, String body) {
        // 1. Save Notification entity to MySQL Database
        // notificationRepository.save(new Notification(recipient, title, body));
        
        // 2. Dispatch Push Notification (Placeholder)
        System.out.println(">>> [FCM PUSH] Sending to User ID " + recipient.getId());
        System.out.println(">>> Title: " + title);
        System.out.println(">>> Body: " + body);
    }
    
    public void handleDonorApproval(BloodRequest request, User donor) {
        // Update request status
        request.setStatus("APPROVED");
        
        // Here we trigger the security logic where donor contact info is unlocked for the patient
        // Because the status is APPROVED, the DTO mapper will now include the phone number.
        
        sendNotification(
            request.getPatient(), 
            "Blood Request Accepted!", 
            donor.getFullName() + " has accepted your request. Tap to view live location and contact details."
        );
    }
}
