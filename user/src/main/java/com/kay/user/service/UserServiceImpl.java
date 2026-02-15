package com.kay.user.service;


import com.kay.user.dto.*;
import com.kay.user.exception.ApiException;
import com.kay.user.exception.NoUserExistException;
import com.kay.user.exception.UserExistException;
import com.kay.user.model.Role;
import com.kay.user.model.Status;
import com.kay.user.model.Users;
import com.kay.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final KeycloakService keycloakService;
    private final String cacheName = "user";


    @Override
    @CachePut(cacheNames = cacheName,key="#result.userId")
    public UserResponseDto registerUser(UserRequestDto requestDto) {

        userRepository.findUsersByEmail(requestDto.getEmail()).ifPresent(usr-> {
            if(usr.getStatus().equals(Status.INACTIVE)){
                usr.setStatus(Status.ACTIVE);
            }
            userRepository.save(usr);
            throw new UserExistException(usr.getUserName());
        });

        String token = keycloakService.getAdminToken();

        String keyCloakId = keycloakService.createUser(token,requestDto);
        Users user = getUsers(requestDto);
        if(keyCloakId != null){
            user.setKeycloakId(keyCloakId);
        }

        Users savedUser = userRepository.save(user);

        keycloakService.assignRoleToUserOrRealm(savedUser.getUserName(),user.getRole().name(),keyCloakId);

        return modelMapper.map(savedUser,UserResponseDto.class);
    }



    @Override
    @Cacheable(cacheNames = cacheName, key = "#userId")
    public UserResponseDto userById(String userId) {
        Users user = userRepository.findById(userId).orElseThrow(()->
                new NoUserExistException(userId));

        return modelMapper.map(user,UserResponseDto.class);
    }

    @Override
    public UserPaginatedResponseDto findAllUsers(String roles, String status, String pageNumber, String pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(Integer.parseInt(pageNumber),Integer.parseInt(pageSize),sort);

        Role strRole = null;
        Status strStatus = Status.ACTIVE;

        switch (status.toUpperCase()){
            case "SUSPENDED" ->
                strStatus = Status.SUSPENDED;
            case "INACTIVE" ->
                strStatus = Status.INACTIVE;
        }

        switch(roles.toUpperCase()){
            case "EMPLOYER" ->{
                strRole = Role.EMPLOYER;
            }
            case "ADMIN" ->
                strRole = Role.ADMIN;
            default ->
                strRole = Role.USER;
        }

        Optional<Page<Users>> userList = userRepository.findAllByRoleOrStatus(strRole,strStatus,pageable);

        UserPaginatedResponseDto userResponse = new UserPaginatedResponseDto();

        userList.ifPresentOrElse(
                usr -> {
                    List<UserResponseDto> uResponse =
                            usr.getContent()
                                    .stream()
                                    .map(user -> modelMapper.map(user, UserResponseDto.class))
                                    .toList();
                    userResponse.setUserList(uResponse);
                    userResponse.setPageNumber(usr.getNumber());
                    userResponse.setPageSize(usr.getSize());
                    userResponse.setTotalPages(usr.getTotalPages());
                    userResponse.setTotalElements(usr.getTotalElements());
                    userResponse.setLast(usr.isLast());
                },
                () -> {
                    throw new ApiException("User not found");
                }


        );

        return userResponse;
    }

    @Override
    @CachePut(cacheNames = cacheName,key = "#userId")
    public UserResponseDto updateUser(String userId, ApplicantRequestDto requestDto) {
        Users savedUser = userRepository.findById(userId).orElseThrow(()->
                new NoUserExistException(userId));

        savedUser.setFirstName(requestDto.getFirstName());
        savedUser.setLastName(requestDto.getLastName());
        savedUser.setPhoneNumber(requestDto.getPhoneNumber());
        savedUser.setEmail(requestDto.getEmail());

        userRepository.save(savedUser);

        keycloakService.UpdateUser(savedUser.getKeycloakId(),requestDto);

        return modelMapper.map(savedUser,UserResponseDto.class);
    }

    // used by: Employer,Job_Seeker
    @Override
    @CacheEvict(cacheNames = cacheName, key="#userId")
    public String deleteUser(String userId) {
        Users savedUser = userRepository.findById(userId).orElseThrow(()->
                new NoUserExistException(userId));

        keycloakService.deleteUser(savedUser.getKeycloakId());

        userRepository.deleteById(userId);
        return "User Deleted successfully";
    }

    @Override
    @CachePut(cacheNames = cacheName, key = "#userId")
    public UserResponseDto deactivateUser(String userId) {
        Users modified = getUsers(userId,Status.INACTIVE);

        keycloakService.deactivateUser(modified.getKeycloakId());

        return modelMapper.map(modified,UserResponseDto.class);
    }


    @Override
    @CachePut(cacheNames = cacheName, key = "#userId")
    public UserResponseDto activateUser(String userId) {
        Users modified = getUsers(userId,Status.ACTIVE);

        keycloakService.activateUser(modified.getKeycloakId());

        return modelMapper.map(modified,UserResponseDto.class);
    }

    @Override
    public boolean resetPassword(String userId, ResetPassword resetPassword) {

        Users savedUser = userRepository.findById(userId).orElseThrow(()->
                new NoUserExistException(userId));

        return keycloakService.resetPassword(savedUser.getKeycloakId(),resetPassword);

    }

    private Users getUsers(String userId,Status status) {
        Users savedUser = userRepository.findById(userId).orElseThrow(()->
                new NoUserExistException(userId));

        savedUser.setStatus(status);
        return userRepository.save(savedUser);
    }

    private static Users getUsers(UserRequestDto requestDto) {
        Role role;

        if(requestDto.getRole()==null){
            role = Role.USER;
        }
        else {
            role = switch (requestDto.getRole().toUpperCase()) {
                case "EMPLOYER" -> Role.EMPLOYER;
                case "ADMIN" -> Role.ADMIN;
                default -> Role.USER;
            };
        }

        Users user = new Users();
        user.setUserName(requestDto.getUserName());
        user.setEmail(requestDto.getEmail());
        user.setPhoneNumber(requestDto.getPhoneNumber());
        user.setRole(role);
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        return user;
    }

}
