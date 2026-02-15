package com.kay.user.dto;


import com.kay.user.model.Role;
import com.kay.user.model.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponseDto {
    private String userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String keycloakId;
    private Status status;
    private Role role;
    private LocalDateTime createdAt;
}
