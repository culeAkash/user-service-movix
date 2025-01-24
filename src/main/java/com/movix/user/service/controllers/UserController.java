package com.movix.user.service.controllers;

import com.movix.user.service.dto.UserDTO;
import com.movix.user.service.exceptions.DuplicateEntryException;
import com.movix.user.service.requests.UserRegisterRequest;
import com.movix.user.service.requests.UserUpdateRequest;
import com.movix.user.service.responses.GenericApiResponse;
import com.movix.user.service.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RefreshScope
@AllArgsConstructor
public class UserController {
    private UserService userService;
    // Get requests

    @PostMapping("/createUser")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserRegisterRequest request) throws DuplicateEntryException {
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
        System.out.println("Service" +" " + this.userService);
        List<UserDTO> userDTOs = this.userService.getAllUsers();
        return ResponseEntity.ok(userDTOs);
    }

    // Update user
    @PatchMapping("updateUser")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserUpdateRequest request) {
        UserDTO userDTO = this.userService.updateUser(request);
        return ResponseEntity.ok(userDTO);
    }

    // Delete User
    @DeleteMapping("deleteUser")
    public ResponseEntity<Void> deleteUser(@RequestParam("userId") String userId) {
        this.userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    //TODO : Add controller to check username validation in real time
}
