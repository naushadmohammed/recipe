package com.abnamro.nl.recipe.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * RecipeCreateDTO
 * DTO for recipe creation request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeCreateDTO {

    @NotEmpty
    @Size(min = 5, message = "recipe name should be at least 5 characters")
    private String recipeName;
    @Positive
    private long prepTimeInMinutes;
    @Positive
    private long cookingTimeInMinutes;

    private boolean vegan;
    private boolean vegetarian;
    @Positive
    private long serves;

    @NotEmpty
    @Size(min = 3, message = "Cuisine name should be at least 3 characters")
    private String cuisine;

    @NotEmpty
    @Size(min = 3, message = "Course should be at least 3 characters")
    private String course;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    @NotEmpty
    @Valid
    private List<IngredientsDTO> ingredients;
    @NotEmpty
    @Valid
    private List<InstructionDTO> instructions;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InstructionDTO {

        @Positive
        private long instructionNumber;
        @NotEmpty
        @Size(min = 3, message = "Instruction Text should be at least 3 characters")
        private String instructionText;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IngredientsDTO {
        @NotEmpty
        private String name;
        @NotEmpty
        private String unit;
        @Positive
        private double quantity;
    }

}
