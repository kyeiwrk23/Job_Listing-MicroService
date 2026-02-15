package com.kay.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserPaginatedResponseDto {
    private List<UserResponseDto> userList;
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private Long totalElements;
    private boolean isLast;
}
