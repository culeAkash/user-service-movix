package com.movix.user.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private String userId;
    private String firstName;
    private String lastName;
    private String emailId;
    private String username;
    private String aboutMe;
}
