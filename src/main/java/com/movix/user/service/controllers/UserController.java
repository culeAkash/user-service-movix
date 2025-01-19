package com.movix.user.service.controllers;

import com.movix.user.service.dto.UserDTO;
import com.movix.user.service.requests.UserRegisterRequest;
import com.movix.user.service.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private UserService userService;



    @GetMapping("/createUser")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserRegisterRequest request) {
        UserDTO savedUserDTO = this.userService.createUser(request);
        return ResponseEntity.ok(savedUserDTO);
    }
}
