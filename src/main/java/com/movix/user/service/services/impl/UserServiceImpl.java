package com.movix.user.service.services.impl;

import com.movix.user.service.dto.UserDTO;
import com.movix.user.service.entities.User;
import com.movix.user.service.enums.Active;
import com.movix.user.service.enums.Role;
import com.movix.user.service.exceptions.GenericException;
import com.movix.user.service.exceptions.ResourceNotFoundException;
import com.movix.user.service.repositories.UserRepository;
import com.movix.user.service.requests.UserRegisterRequest;
import com.movix.user.service.requests.UserUpdateRequest;
import com.movix.user.service.services.UserService;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Getter
@Data
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ModelMapper modelMapper;

    private PasswordEncoder passwordEncoder;

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    @Override
    public UserDTO createUser(UserRegisterRequest request) {
        try {
            User toSaveUser = User.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .emailId(request.getEmailId())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .aboutMe(request.getAboutMe())
                    .active(Active.ACTIVE)
                    .role(Role.USER)
                    .build();

            User savedUser = this.userRepository.save(toSaveUser);

            return this.modelMapper.map(savedUser, UserDTO.class);
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            throw new GenericException("Failed to create new user",HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public UserDTO getUserById(String userId) {
       User userByUserId = findUserById(userId);
       return this.modelMapper.map(userByUserId, UserDTO.class);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> allUsers = this.userRepository.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();
        allUsers.forEach(user -> {
            UserDTO userDTO = this.modelMapper.map(user, UserDTO.class);
            userDTOs.add(userDTO);
        });
        return userDTOs;
    }

    @Override
    public UserDTO updateUser(UserUpdateRequest request) {
        try {
            User toUpdateUser = findUserById(request.getUserId());

            this.modelMapper.map(request, toUpdateUser);
            User updatedUser = this.userRepository.save(toUpdateUser);

            return this.modelMapper.map(updatedUser, UserDTO.class);
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            throw new GenericException("Failed to update user",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteUser(String userId) {
        User toDeleteUser = findUserById(userId);
        try {
            this.userRepository.delete(toDeleteUser);
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            throw new GenericException("Failed to delete user with userId : " + userId, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // utility methods
    private User findUserById(String userId) {
        return this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }
}
