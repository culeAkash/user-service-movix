package com.movix.user.service.controllers;

import com.movix.user.service.dto.UserDTO;
import com.movix.user.service.requests.UserRegisterRequest;
import com.movix.user.service.requests.UserUpdateRequest;
import com.movix.user.service.responses.GenericApiResponse;
import com.movix.user.service.services.BloomFilterService;
import com.movix.user.service.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private UserService userService;
    private BloomFilterService bloomFilterService;
    // Get requests

    @GetMapping("/createUser")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserRegisterRequest request) {
        UserDTO savedUserDTO = this.userService.createUser(request);
        return ResponseEntity.ok(savedUserDTO);
    }

    @GetMapping("/getUserById")
    public ResponseEntity<UserDTO> getUserById(@RequestParam("userId") String userId) {
        UserDTO userDTO = this.userService.getUserById(userId);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOs = this.userService.getAllUsers();
        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/checkUsername")
    public ResponseEntity<GenericApiResponse> checkUsername(@RequestParam("username") String username) {
        boolean isValid = this.userService.validateUsername(username);
        return ResponseEntity.ok(GenericApiResponse.builder()
                        .message(isValid ? "Username is valid" : "Username is invalid")
                        .success(isValid)
                .build());
    }

    // Update user
    @PatchMapping("updateUser")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserUpdateRequest request) {
        UserDTO userDTO = this.userService.updateUser(request);
        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping("deleteUser")
    public ResponseEntity<Void> deleteUser(@RequestParam("userId") String userId) {
        this.userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
