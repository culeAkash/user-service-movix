package com.movix.user.service.services.impl;

import com.movix.user.service.dto.UserDTO;
import com.movix.user.service.entities.User;
import com.movix.user.service.enums.Active;
import com.movix.user.service.enums.Role;
import com.movix.user.service.repositories.UserRepository;
import com.movix.user.service.requests.UserRegisterRequest;
import com.movix.user.service.services.UserService;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Getter
@Data
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private ModelMapper modelMapper;

    private PasswordEncoder passwordEncoder;


    @Override
    public UserDTO createUser(UserRegisterRequest request) {
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
}
