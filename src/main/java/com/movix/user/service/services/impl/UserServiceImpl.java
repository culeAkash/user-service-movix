package com.movix.user.service.services.impl;

import com.movix.user.service.dto.UserDTO;
import com.movix.user.service.entities.User;
import com.movix.user.service.enums.Active;
import com.movix.user.service.enums.Role;
import com.movix.user.service.exceptions.DuplicateEntryException;
import com.movix.user.service.exceptions.GenericException;
import com.movix.user.service.exceptions.ResourceNotFoundException;
import com.movix.user.service.repositories.UserRepository;
import com.movix.user.service.requests.UserRegisterRequest;
import com.movix.user.service.requests.UserUpdateRequest;
import com.movix.user.service.services.UserService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
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
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ModelMapper modelMapper;

    private PasswordEncoder passwordEncoder;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);


    @Override
    public UserDTO createUser(UserRegisterRequest request) throws DuplicateEntryException {

            // Not efficient has to be replaced with Bloom filter checking later
            User existingUserByUsername = this.userRepository.findByUsername(request.getUsername()).orElse(null);
            if (existingUserByUsername != null) {
                throw new DuplicateEntryException("User", "username", request.getUsername());
            }

            // Not efficient has to be replaced with Bloom filter checking later
            User existingUserByEmail = this.userRepository.findByEmailId(request.getEmailId()).orElse(null);
            if (existingUserByEmail != null) {
                throw new DuplicateEntryException("User", "emailId", request.getEmailId());
            }


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

        User toUpdateUser = findUserById(request.getUserId());

        this.modelMapper.map(request, toUpdateUser);
//        LOGGER.info("Updated user : {}", request.getLastName());
        User updatedUser = this.userRepository.save(toUpdateUser);

        return this.modelMapper.map(updatedUser, UserDTO.class);
    }



    @Override
    public void deleteUser(String userId) {
        User toDeleteUser = findUserById(userId);
        this.userRepository.delete(toDeleteUser);
    }


    // utility methods
    private User findUserById(String userId) {
        return this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }
}

