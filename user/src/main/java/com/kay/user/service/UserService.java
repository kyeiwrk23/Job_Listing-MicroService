package com.kay.user.service;


import com.kay.user.dto.*;

public interface UserService {
    UserResponseDto registerUser(UserRequestDto requestDto);

    UserResponseDto userById(String userId);

    UserPaginatedResponseDto findAllUsers(String roles, String status, String pageNumber, String pageSize, String sortBy, String sortOrder);

    UserResponseDto updateUser(String userId, ApplicantRequestDto requestDto);

    String deleteUser(String userId);

    UserResponseDto deactivateUser(String userId);

    UserResponseDto activateUser(String userId);

    boolean resetPassword(String userId, ResetPassword resetPassword);
}
