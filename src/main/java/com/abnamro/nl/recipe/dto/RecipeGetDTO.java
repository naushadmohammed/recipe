package com.abnamro.nl.recipe.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * RecipeGetDTO
 * DTO for Recipe Get provides detailed recipe details
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeGetDTO {

    private long recipeId;

    private String recipeName;
    private long prepTimeInMinutes;
    private long cookingTimeInMinutes;

    private boolean vegan;
    private boolean vegetarian;
    private long serves;

    private String cuisine;

    private String course;

    @Schema(required = true,example = "2016-01-01 10:11:12")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    private List<IngredientsDTO> ingredients;
    private List<InstructionDTO> instructions;
    private String createdBy;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InstructionDTO {

        private long instructionNumber;
        private String instructionText;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IngredientsDTO {
        private String name;
        private String unit;
        private double quantity;
    }

}
