package com.abnamro.nl.recipe.repos;

import com.abnamro.nl.recipe.entities.Instruction;
import com.abnamro.nl.recipe.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructionsRepository extends JpaRepository<Instruction, Long> {

    void deleteByRecipe(Recipe recipeId);
}
