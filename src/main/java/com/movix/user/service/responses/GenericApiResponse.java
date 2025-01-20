package com.movix.user.service.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

@Data
@Builder
@AllArgsConstructor
public class GenericApiResponse {
    private String message;
    private boolean success;
}
