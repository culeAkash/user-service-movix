package com.movix.user.service.services;

import com.movix.user.service.dto.UserDTO;
import com.movix.user.service.requests.UserRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

public interface UserService {

    public UserDTO createUser(UserRegisterRequest request);

}
