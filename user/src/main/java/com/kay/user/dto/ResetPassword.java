package com.kay.user.dto;

import lombok.Data;

@Data
public class ResetPassword {
    private String newPassword;
    private String oldPassword;
}
