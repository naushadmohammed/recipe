package com.abnamro.nl.recipe.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * User
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @NotEmpty
    @Size(min= 8,message = "User name should be at least 8 characters long")
    private String userName;
    @NotEmpty
    @Size(min= 8,message = "Password name should be at least 8 characters long")
    private String password;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "createdBy")
    private List<Recipe> createdRecipeList;

}
