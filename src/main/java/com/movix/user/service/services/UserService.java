package com.movix.user.service.services;

import com.movix.user.service.dto.UserDTO;
import com.movix.user.service.requests.UserRegisterRequest;
import com.movix.user.service.requests.UserUpdateRequest;

import java.util.List;

public interface UserService {

    public UserDTO createUser(UserRegisterRequest request);

    public UserDTO getUserById(String userId);

    public List<UserDTO> getAllUsers();


    // Method for updating the user
    public UserDTO updateUser(UserUpdateRequest request);


    public void deleteUser(String userId);

}
