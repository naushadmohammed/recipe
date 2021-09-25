package com.abnamro.nl.recipe.repos;

import com.abnamro.nl.recipe.entities.Ingredients;
import com.abnamro.nl.recipe.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientsRepo extends JpaRepository<Ingredients, Long> {

    void deleteByRecipe(Recipe recipeId);
}
