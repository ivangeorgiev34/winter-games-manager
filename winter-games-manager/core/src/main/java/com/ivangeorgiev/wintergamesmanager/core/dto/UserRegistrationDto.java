package com.ivangeorgiev.wintergamesmanager.core.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class UserRegistrationDto {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}