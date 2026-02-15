package com.kay.user.controller;


import com.kay.user.constant.AppConstant;
import com.kay.user.dto.*;
import com.kay.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RefreshScope
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserServiceImpl userService;


    // used for:Employer,Admin,job_seeker

    @PostMapping
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody UserRequestDto requestDto){
        UserResponseDto responseDto = userService.registerUser(requestDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    //Used by: Employer,Admin,job_seeker
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> userById(@PathVariable String userId){
        UserResponseDto responseDto = userService.userById(userId);

        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

    //Used by: Admin only
    @GetMapping
    public ResponseEntity<UserPaginatedResponseDto> findAllUsers(
            @RequestParam(name = "roles", required = false,defaultValue = AppConstant.ROLES) String roles,
            @RequestParam(name = "status",required = false,defaultValue = AppConstant.STATUS) String status,
            @RequestParam(name = "pageNumber", required = false,defaultValue = AppConstant.PAGENUMBER) String pageNumber,
            @RequestParam(name = "pageSize", required = false,defaultValue = AppConstant.PAGESIZE) String pageSize,
            @RequestParam(name = "sortBy", required = false,defaultValue = AppConstant.SORTBY) String sortBy,
            @RequestParam(name = "sortOrder", required = false,defaultValue = AppConstant.SORTORDER) String sortOrder){

        UserPaginatedResponseDto users =
                userService.findAllUsers(roles,status,pageNumber,pageSize,sortBy,sortOrder);

        return new ResponseEntity<>(users,HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable String userId, @RequestBody ApplicantRequestDto requestDto){
        UserResponseDto uResponse = userService.updateUser(userId,requestDto);

        return new ResponseEntity<>(uResponse,HttpStatus.OK);
    }

    // used by: Admin
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId){
        String deactivateUser  = userService.deleteUser(userId);

        return  new ResponseEntity<>(deactivateUser, HttpStatus.OK);
    }

    //Used by: Employer,Job_Seeker
    @PutMapping("/{userId}/suspend")
    public ResponseEntity<UserResponseDto> deactivateUsers(@PathVariable String userId){
        UserResponseDto suspendedUser =  userService.deactivateUser(userId);

        return new ResponseEntity<>(suspendedUser,HttpStatus.OK);
    }

    //Used by: Admin
    @PutMapping("/{userId}/activate")
    public ResponseEntity<UserResponseDto> activateUsers(@PathVariable String userId){
        UserResponseDto suspendedUser =  userService.activateUser(userId);

        return new ResponseEntity<>(suspendedUser,HttpStatus.OK);
    }

    //Reset Password
    @PutMapping("/{userId}/reset-password")
    public ResponseEntity<String> resetPassword(@PathVariable String userId, @RequestBody ResetPassword resetPassword){

        boolean success = userService.resetPassword(userId,resetPassword);
        if(success){
            return new ResponseEntity<>("Password Reset Successfully!!!",HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
