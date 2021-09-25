package com.abnamro.nl.recipe.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

/**
 * Ingredients
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Ingredients {

    @Id
    @GeneratedValue
    private long ingredientId;

    @NotEmpty
    private String name;
    @NotEmpty
    private String unit;
    @Positive
    private double quantity;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
}
