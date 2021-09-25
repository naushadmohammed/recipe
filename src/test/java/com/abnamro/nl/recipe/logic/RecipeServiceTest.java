package com.abnamro.nl.recipe.logic;


import com.abnamro.nl.recipe.entities.Ingredients;
import com.abnamro.nl.recipe.entities.Instruction;
import com.abnamro.nl.recipe.entities.Recipe;
import com.abnamro.nl.recipe.entities.User;
import com.abnamro.nl.recipe.repos.IngredientsRepo;
import com.abnamro.nl.recipe.repos.InstructionsRepository;
import com.abnamro.nl.recipe.repos.RecipeRepository;
import com.abnamro.nl.recipe.repos.UserRepository;
import com.abnamro.nl.recipe.utils.RecipeAppConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@RunWith(SpringRunner.class)
public class RecipeServiceTest {


    @MockBean
    private RecipeRepository recipeRepository;
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private InstructionsRepository instructionsRepository;

    @MockBean
    private IngredientsRepo ingredientsRepo;

    @InjectMocks
    private RecipeService recipeService;

    private static final String RECIPE_NAME = "Test Recipe Name";
    private static final String COURSE = "main";
    private static final String CUISINE = "American";

    private static final String TOMATO = "tomato";
    private static final String UNIT = "KG";

    private static final String INSTRUCTION_TEXT = "Slice Tomato";


    private static final List<Ingredients> ingredientsList = Collections.singletonList(new Ingredients(1, TOMATO, UNIT, 1, null));
    private static final List<Instruction> instructions = Collections.singletonList(new Instruction(1, 1, INSTRUCTION_TEXT, null));

    Recipe mockRecipe;
    User mockUser;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockUser = new User("TestUser12345", "TestPassword", null);
        mockRecipe = new Recipe(1, RECIPE_NAME, 10, 20, true, true, CUISINE, COURSE, 3, LocalDateTime.now(), ingredientsList, instructions, mockUser);
    }

    @Test
    public void test_CreateRecipe_UserDoesNotExists() {
        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () -> recipeService.createRecipe(mockRecipe, "TestUser"));
        assertEquals(HttpStatus.UNAUTHORIZED, responseStatusException.getStatus());
        assertEquals(RecipeAppConstants.USER_NOT_FOUND, responseStatusException.getReason());
    }


    @Test
    public void test_CreateRecipe_UserExists() {
        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(mockUser));
        Mockito.when(recipeRepository.save(Mockito.any())).thenReturn(mockRecipe);
        recipeService.createRecipe(mockRecipe, "Test");
        Mockito.verify(userRepository, Mockito.times(1)).findById("Test");
        Mockito.verify(recipeRepository, Mockito.times(1)).save(mockRecipe);
    }

    @Test
    public void test_GetRecipe_RecipeExists() {
        Mockito.when(recipeRepository.findById(mockRecipe.getRecipeId())).thenReturn(Optional.of(mockRecipe));
        recipeService.getRecipe(mockRecipe.getRecipeId());
        Mockito.verify(recipeRepository, Mockito.times(1)).findById(mockRecipe.getRecipeId());
    }

    @Test
    public void test_GetRecipe_RecipeDoesNotExists() {
        Mockito.when(recipeRepository.findById(mockRecipe.getRecipeId())).thenReturn(Optional.empty());
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () -> {
            recipeService.getRecipe(mockRecipe.getRecipeId());
        });
        assertEquals(HttpStatus.NOT_FOUND, responseStatusException.getStatus());
        assertEquals(RecipeAppConstants.RECIPE_NOT_FOUND, responseStatusException.getReason());
    }

    @Test
    public void test_DeleteRecipe_UserDoesNotExist() {
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () -> {
            recipeService.deleteRecipe(mockRecipe.getRecipeId(), "TestUser");
        });
        assertEquals(HttpStatus.UNAUTHORIZED, responseStatusException.getStatus());
        assertEquals(RecipeAppConstants.USER_NOT_FOUND, responseStatusException.getReason());

    }

    @Test
    public void test_DeleteRecipe_UserExists_RecipeDoesNotExists() {
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(mockUser));
        Mockito.when(recipeRepository.findById(mockRecipe.getRecipeId())).thenReturn(Optional.empty());
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () -> {
            recipeService.deleteRecipe(mockRecipe.getRecipeId(), "TestUser");
        });
        assertEquals(HttpStatus.NOT_FOUND, responseStatusException.getStatus());
        assertEquals(RecipeAppConstants.RECIPE_NOT_FOUND, responseStatusException.getReason());

    }

    @Test
    public void test_deleteRecipe_UserExistsButNotOwner_RecipeExists() {
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(new User("UnAuthTestUser","TestPassword",null)));
        Mockito.when(recipeRepository.findById(mockRecipe.getRecipeId())).thenReturn(Optional.of(mockRecipe));
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () -> {
            recipeService.deleteRecipe(mockRecipe.getRecipeId(), "UnAuthTestUser");
        });
        assertEquals(HttpStatus.UNAUTHORIZED, responseStatusException.getStatus());
        assertEquals(RecipeAppConstants.USER_NOT_ALLOWED_OPERATION, responseStatusException.getReason());

    }

    @Test
    public void test_UpdateRecipe_UserDoesNotExist() {
        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () -> {
            recipeService.updateRecipe(mockRecipe, "TestUser");
        });
        assertEquals(HttpStatus.UNAUTHORIZED, responseStatusException.getStatus());
        assertEquals(RecipeAppConstants.USER_NOT_FOUND, responseStatusException.getReason());
    }

    @Test
    public void test_UpdateRecipe_UserExist_RecipeDoesNotExist() {
        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(mockUser));
        Mockito.when(recipeRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        ResponseStatusException responseStatusException;
        responseStatusException = assertThrows(ResponseStatusException.class, () -> {
            recipeService.updateRecipe(mockRecipe, mockUser.getUserName());
        });
        assertEquals(HttpStatus.NOT_FOUND, responseStatusException.getStatus());
        assertEquals(RecipeAppConstants.RECIPE_NOT_FOUND, responseStatusException.getReason());
    }

    @Test
    public void test_UpdateRecipe_UserExist_RecipeExist() {
        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(mockUser));
        Mockito.when(recipeRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockRecipe));
        Mockito.when(recipeRepository.save(Mockito.any())).thenReturn(mockRecipe);

        Mockito.doNothing().when(ingredientsRepo).deleteByRecipe(Mockito.any());
        Mockito.doNothing().when(instructionsRepository).deleteByRecipe(Mockito.any());

        recipeService.updateRecipe(mockRecipe, mockUser.getUserName());

        Mockito.verify(ingredientsRepo, Mockito.times(1)).deleteByRecipe(Mockito.any());
        Mockito.verify(instructionsRepository, Mockito.times(1)).deleteByRecipe(Mockito.any());
        Mockito.verify(recipeRepository, Mockito.times(1)).save(Mockito.any());


    }

}
