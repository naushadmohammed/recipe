package com.abnamro.nl.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * RecipeListDTO
 * DTO for Recipe Get provides brief recipe details
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeListDTO {

    private long recipeId;
    private String recipeName;
    private long prepTimeInMinutes;
    private long cookingTimeInMinutes;
    private int serves;
    private String cuisine;
    private String course;
    private LocalDateTime createdOn;
    private String createdBy;

}
