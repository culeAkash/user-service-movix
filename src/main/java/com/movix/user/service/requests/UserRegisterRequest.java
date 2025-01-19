package com.movix.user.service.requests;


import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
public class UserRegisterRequest {
    @Size(min = 6,max = 20, message = "Username must be at least 6 characters and not more than 20 characters")
    private String firstName;

    @Size(min = 6,max = 20, message = "Username must be at least 6 characters and not more than 20 characters")
    private String lastName;

    @NotBlank(message = "Username is required")
    @Size(min = 6, message = "Username must be at least 6 characters")
    private String username;

    @Email(message = "Email should be valid")
    private String emailId;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "Password must be at least 8 characters and contain at least one letter and one number")
    @NotNull(message = "Password is required")
    private String password;

    @Size(min = 5,max = 100,message = "Write more than 5 characters and less than 100 characters about yourself")
    private String aboutMe;
}
