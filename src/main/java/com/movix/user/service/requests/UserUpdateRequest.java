package com.movix.user.service.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserUpdateRequest {

    @NotBlank(message = "Id is required")
    private String userId;

    private String firstName;
    private String lastName;

    private String username;

    private String aboutMe;
}
