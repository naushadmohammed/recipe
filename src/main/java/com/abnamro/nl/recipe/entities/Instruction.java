package com.abnamro.nl.recipe.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

/**
 * Instruction
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Instruction {

    @Id
    @GeneratedValue
    private long instructionId;
    @Positive
    private long instructionNumber;
    @NotEmpty
    @Size(min = 3, message = "Instruction Text should be at least 3 characters")
    private String text;

    @ManyToOne
    @JoinColumn(name = "fk_recipe")
    private Recipe recipe;
}
