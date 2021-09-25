package com.abnamro.nl.recipe.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Recipe
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Recipe {
    @Id
    @GeneratedValue
    private long recipeId;

    @NotEmpty
    @Size(min = 5, message = "recipe name should be at least 5 characters")
    private String recipeName;
    @Positive
    private long prepTimeInMinutes;
    @Positive
    private long cookingTimeInMinutes;
    private boolean vegetarian;
    private boolean vegan;

    @NotEmpty
    @Size(min = 3, message = "Cuisine name should be at least 3 characters")
    private String cuisine;

    @NotEmpty
    @Size(min = 3, message = "Course should be at least 3 characters")
    private String course;

    @Positive
    private long serves;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    @NotEmpty
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<Ingredients> ingredientsList;

    @NotEmpty
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<Instruction> instructionsList;

    @ManyToOne
    @JoinColumn(name = "fk_user")
    private User createdBy;
}
