package com.kay.user.dto;

import lombok.Data;

@Data
public class UserRequestDto {

    private String userName;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String password;
    private String email;
    private String status;
    private String role;
}
