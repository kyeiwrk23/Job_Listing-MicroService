package com.kay.submitApplication.external;


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
    private Status status;
    private Role role;
    private LocalDateTime createdAt;
}
