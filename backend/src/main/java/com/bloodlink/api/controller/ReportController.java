package com.bloodlink.api.controller;

import com.bloodlink.api.entity.User;
import com.bloodlink.api.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    private final UserRepository userRepository;

    public ReportController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/csv")
    public ResponseEntity<byte[]> generateCSVReport() {
        List<User> users = userRepository.findAll();
        
        StringBuilder csv = new StringBuilder("ID,Full Name,Role,Blood Group,Phone,City,Registration Date\n");
        for (User u : users) {
            csv.append(u.getId()).append(",")
               .append(u.getFullName()).append(",")
               .append(u.getRole()).append(",")
               .append(u.getBloodGroup()).append(",")
               .append(u.getPhone()).append(",")
               .append(u.getCity()).append(",")
               .append(u.getCreatedAt()).append("\n");
        }
        
        byte[] bytes = csv.toString().getBytes();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "bloodlink_report.csv");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(bytes);
    }
}
