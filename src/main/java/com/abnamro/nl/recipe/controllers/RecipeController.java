package com.abnamro.nl.recipe.controllers;

import com.abnamro.nl.recipe.dto.RecipeCreateDTO;
import com.abnamro.nl.recipe.dto.RecipeGetDTO;
import com.abnamro.nl.recipe.dto.RecipeListDTO;
import com.abnamro.nl.recipe.dto.RecipeUpdateDTO;
import com.abnamro.nl.recipe.entities.Recipe;
import com.abnamro.nl.recipe.logic.RecipeService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/recipe")
@AllArgsConstructor
/**
 * RecipeController
 * Acts as a REST Controller
 */
public class RecipeController {


    private ModelMapper modelMapper;
    private RecipeService recipeService;


    /**
     * Gets list of all Recipes
     * @param size defaults to 10
     * @param page defaults to 0
     * @return List of all recipes in page
     */
    @GetMapping
    public List<RecipeListDTO> getRecipes(
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "page", defaultValue = "0") int page) {
        return recipeService.getRecipes(page, size).
                stream().
                map(result -> modelMapper.map(result, RecipeListDTO.class)).
                collect(Collectors.toList());
    }

    /**
     * Create recipe and returns created recipe
     * @param recipeToCreate recipe details for creation
     * @param principal spring security authenticated user
     * @return created recipe
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RecipeGetDTO createRecipe(@Valid @RequestBody RecipeCreateDTO recipeToCreate, Principal principal) {
        Recipe recipe = modelMapper.map(recipeToCreate, Recipe.class);
        recipe.setCreatedOn(LocalDateTime.now().withNano(0));
        return modelMapper.map(recipeService.createRecipe(recipe, principal.getName()), RecipeGetDTO.class);

    }

    /**
     * Gets detail of recipe with instructions and ingredients
     * @param recipeId recipeId to get detailed recipe
     * @return detailed recipe with instructions and ingredients
     */
    @GetMapping("{recipeId}")
    @ResponseStatus(HttpStatus.OK)
    public RecipeGetDTO getRecipe(@PathVariable("recipeId") long recipeId) {
        return modelMapper.map(recipeService.getRecipe(recipeId), RecipeGetDTO.class);
    }

    /**
     *  Updates recipe with provided details only if the requester is also creator of recipe
     * @param recipeId recipeId to update  recipe
     * @param recipeUpdateDTO recipe details to update  recipe
     * @param principal spring security authenticated user
     * @return
     */
    @PutMapping("{recipeId}")
    public RecipeGetDTO updateRecipe(@PathVariable long recipeId, @RequestBody @Valid RecipeUpdateDTO recipeUpdateDTO, Principal principal) {
        Recipe recipeToUpdate = modelMapper.map(recipeUpdateDTO, Recipe.class);
        recipeToUpdate.setRecipeId(recipeId);
        return modelMapper.map(recipeService.updateRecipe(recipeToUpdate, principal.getName()), RecipeGetDTO.class);
    }

    /**
     * Deletes recipe only if the requester is also creator of recipe
     * @param recipeId
     * @param principal
     */
    @DeleteMapping("{recipeId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteRecipe(@PathVariable("recipeId") long recipeId, Principal principal) {
        recipeService.deleteRecipe(recipeId, principal.getName());
    }

}
