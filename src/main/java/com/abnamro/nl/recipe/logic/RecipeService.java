package com.abnamro.nl.recipe.logic;

import com.abnamro.nl.recipe.entities.Recipe;
import com.abnamro.nl.recipe.entities.User;
import com.abnamro.nl.recipe.repos.IngredientsRepo;
import com.abnamro.nl.recipe.repos.InstructionsRepository;
import com.abnamro.nl.recipe.repos.RecipeRepository;
import com.abnamro.nl.recipe.repos.UserRepository;
import com.abnamro.nl.recipe.utils.RecipeAppConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * RecipeService
 * Contains all the Business logic regarding Recipe entity
 */
@Slf4j
@Component
@AllArgsConstructor
public class RecipeService {


    private RecipeRepository recipeRepository;
    private UserRepository userRepository;
    private InstructionsRepository instructionsRepository;
    private IngredientsRepo ingredientsRepo;

    @CachePut(value = "recipe-cache", key = "#recipe.recipeId")
    public Recipe createRecipe(@Valid Recipe recipe, @NotNull String userName) {
        User user = userRepository.findById(userName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, RecipeAppConstants.USER_NOT_FOUND));
        recipe.getIngredientsList().forEach(ingredients -> ingredients.setRecipe(recipe));
        recipe.getInstructionsList().forEach(instruction -> instruction.setRecipe(recipe));
        recipe.setCreatedBy(user);
        return recipeRepository.save(recipe);
    }

    @Cacheable(value = "recipe-cache", key = "#recipeId")
    public Recipe getRecipe(long recipeId) {
        return recipeRepository.findById(recipeId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, RecipeAppConstants.RECIPE_NOT_FOUND));
    }

    public List<Recipe> getRecipes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return recipeRepository.findAll(pageable).getContent();
    }

    @CacheEvict(value = "recipe-cache", key = "#recipeId")
    public void deleteRecipe(long recipeId, @NotNull String userName) {
        User retrievedUser = userRepository.findById(userName).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, RecipeAppConstants.USER_NOT_FOUND));
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, RecipeAppConstants.RECIPE_NOT_FOUND));

        if (!recipe.getCreatedBy().getUserName().equals(retrievedUser.getUserName())) {
            log.warn("unauthorized user {} trying to update recipe {}", recipeId, retrievedUser.getUserName());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, RecipeAppConstants.USER_NOT_ALLOWED_OPERATION);
        }
        recipeRepository.deleteById(recipeId);
    }

    @Transactional
    @CachePut(value = "recipe-cache", key = "#recipeNew.recipeId")
    public Recipe updateRecipe(@Valid Recipe recipeNew, @NotNull String userName) {

        User user = userRepository.findById(userName).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, RecipeAppConstants.USER_NOT_FOUND));

        if (!user.getUserName().equals(userName)) {
            log.warn("unauthorized user {} trying to update recipe {}", recipeNew.getRecipeId(), userName);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, RecipeAppConstants.USER_NOT_ALLOWED_OPERATION);
        }
        Recipe recipeOld = recipeRepository.findById(recipeNew.getRecipeId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, RecipeAppConstants.RECIPE_NOT_FOUND));
        recipeOld.setRecipeName(recipeNew.getRecipeName());
        recipeOld.setCourse(recipeNew.getCourse());
        recipeOld.setCuisine(recipeNew.getCuisine());
        recipeOld.setPrepTimeInMinutes(recipeNew.getPrepTimeInMinutes());
        recipeOld.setCookingTimeInMinutes(recipeNew.getCookingTimeInMinutes());
        recipeOld.setVegan(recipeNew.isVegan());
        recipeOld.setVegetarian(recipeNew.isVegetarian());
        recipeOld.setCreatedBy(user);

        instructionsRepository.deleteByRecipe(recipeOld);
        ingredientsRepo.deleteByRecipe(recipeOld);
        recipeNew.getIngredientsList().forEach(ingredients -> ingredients.setRecipe(recipeOld));
        recipeNew.getInstructionsList().forEach(instruction -> instruction.setRecipe(recipeOld));
        recipeOld.setIngredientsList(recipeNew.getIngredientsList());
        recipeOld.setInstructionsList(recipeNew.getInstructionsList());
        return recipeRepository.save(recipeOld);
    }
}
