package com.abnamro.nl.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * UserCreateDTO
 * DTO for User creation and login
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO {
    @NotEmpty
    @Size(min= 8,message = "User name should be at least 8 characters long")
    private String userName;

    @NotEmpty
    @Size(min=8,message = "Password should be at least 8 characters long")
    private String password;
}
