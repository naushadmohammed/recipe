package com.abnamro.nl.recipe.controllers;

import com.abnamro.nl.recipe.dto.RecipeCreateDTO;
import com.abnamro.nl.recipe.dto.RecipeGetDTO;
import com.abnamro.nl.recipe.dto.RecipeUpdateDTO;
import com.abnamro.nl.recipe.entities.Ingredients;
import com.abnamro.nl.recipe.entities.Instruction;
import com.abnamro.nl.recipe.entities.Recipe;
import com.abnamro.nl.recipe.logic.RecipeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(value = RecipeController.class)
@WithMockUser
public class RecipeControllerTest {
    private static final String RECIPE_NAME = "Test Recipe Name";
    private static final String COURSE = "main";
    private static final String CUISINE = "American";

    private static final String TOMATO = "tomato";
    private static final String UNIT = "KG";

    private static final String INSTRUCTION_TEXT = "Slice Tomato";


    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RecipeService recipeService;
    @MockBean
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private List<RecipeGetDTO.IngredientsDTO> ingredientsDTOList = Collections.singletonList(new RecipeGetDTO.IngredientsDTO(TOMATO, UNIT, 1));
    private List<RecipeGetDTO.InstructionDTO> instructionDTOList = Collections.singletonList(new RecipeGetDTO.InstructionDTO(1, INSTRUCTION_TEXT));

    List<Ingredients> ingredientsList = Collections.singletonList(new Ingredients(1, TOMATO, UNIT, 1, null));
    List<Instruction> instructions = Collections.singletonList(new Instruction(1, 1, INSTRUCTION_TEXT, null));
    RecipeGetDTO recipeGetDTO;
    Recipe mockRecipe;

    @Before
    public void init() {
        mockRecipe = new Recipe(1, RECIPE_NAME, 10, 20, true, true, CUISINE, COURSE, 3, LocalDateTime.now(), ingredientsList, instructions, null);
    }

    @Test
    public void retrieveRecipe_testMappingDtoToEntity() throws Exception {
        Mockito.when(recipeService.getRecipe(Mockito.anyLong())).thenReturn(mockRecipe);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/recipe/1")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).
                andExpect(MockMvcResultMatchers.status().isOk()).
                andExpect(MockMvcResultMatchers.jsonPath("$.recipeName").value(RECIPE_NAME)).
                andExpect(MockMvcResultMatchers.jsonPath("$.recipeId").value(1)).
                andExpect(MockMvcResultMatchers.jsonPath("$.serves").value(3)).
                andExpect(MockMvcResultMatchers.jsonPath("$.prepTimeInMinutes").value(10)).
                andExpect(MockMvcResultMatchers.jsonPath("$.cookingTimeInMinutes").value(20)).
                andExpect(MockMvcResultMatchers.jsonPath("$.cuisine").value(CUISINE)).
                andExpect(MockMvcResultMatchers.jsonPath("$.course").value(COURSE));
    }

    @Test
    public void retrieveRecipes_testMappingDtoToEntity() throws Exception {
        Mockito.when(recipeService.getRecipes(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Collections.singletonList(mockRecipe));
        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/recipe")
                        .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk()).
                andExpect(MockMvcResultMatchers.jsonPath("$.[0].recipeName").value(RECIPE_NAME)).
                andExpect(MockMvcResultMatchers.jsonPath("$.[0].recipeId").value(1)).
                andExpect(MockMvcResultMatchers.jsonPath("$.[0]serves").value(3)).
                andExpect(MockMvcResultMatchers.jsonPath("$.[0]prepTimeInMinutes").value(10)).
                andExpect(MockMvcResultMatchers.jsonPath("$.[0]cookingTimeInMinutes").value(20)).
                andExpect(MockMvcResultMatchers.jsonPath("$.[0]cuisine").value(CUISINE)).
                andExpect(MockMvcResultMatchers.jsonPath("$.[0]course").value(COURSE));
        ;
    }

    @Test
    public void createRecipe_validRecipe_testMappingEntityToDto() throws Exception {
        Mockito.when(recipeService.createRecipe(Mockito.any(), Mockito.anyString())).thenReturn(mockRecipe);

        RequestBuilder requestBuilder = buildCreateRequest(createRecipeCreateDTO());

        mockMvc.perform(requestBuilder).
                andDo(MockMvcResultHandlers.print()).
                andExpect(MockMvcResultMatchers.status().isCreated()).
                andExpect(MockMvcResultMatchers.jsonPath("$.recipeName").value(RECIPE_NAME)).
                andExpect(MockMvcResultMatchers.jsonPath("$.recipeId").value(1)).
                andExpect(MockMvcResultMatchers.jsonPath("$.serves").value(3)).
                andExpect(MockMvcResultMatchers.jsonPath("prepTimeInMinutes").value(10)).
                andExpect(MockMvcResultMatchers.jsonPath("cookingTimeInMinutes").value(20)).
                andExpect(MockMvcResultMatchers.jsonPath("cuisine").value(CUISINE)).
                andExpect(MockMvcResultMatchers.jsonPath("course").value(COURSE));
        ;
    }

    @Test
    public void createRecipe_inValid_emptyCuisineName() throws Exception {
        RecipeCreateDTO recipeCreateDTO = createRecipeCreateDTO();
        recipeCreateDTO.setCuisine("");
        Mockito.when(recipeService.createRecipe(Mockito.any(), Mockito.anyString())).thenReturn(mockRecipe);
        RequestBuilder requestBuilder = buildCreateRequest(recipeCreateDTO);

        mockMvc.perform(requestBuilder).
                andDo(MockMvcResultHandlers.print()).
                andExpect(MockMvcResultMatchers.status().is4xxClientError())
        ;
    }


    @Test
    public void createRecipe_inValid_emptyCourseName() throws Exception {
        RecipeCreateDTO recipeCreateDTO = createRecipeCreateDTO();
        recipeCreateDTO.setCourse("");
        Mockito.when(recipeService.createRecipe(Mockito.any(), Mockito.anyString())).thenReturn(mockRecipe);
        RequestBuilder requestBuilder = buildCreateRequest(recipeCreateDTO);

        mockMvc.perform(requestBuilder).
                andDo(MockMvcResultHandlers.print()).
                andExpect(MockMvcResultMatchers.status().is4xxClientError())
        ;
    }

    @Test
    public void createRecipe_inValid_recipeName() throws Exception {
        RecipeCreateDTO recipeCreateDTO = createRecipeCreateDTO();
        recipeCreateDTO.setRecipeName("");
        Mockito.when(recipeService.createRecipe(Mockito.any(), Mockito.anyString())).thenReturn(mockRecipe);
        RequestBuilder requestBuilder = buildCreateRequest(recipeCreateDTO);

        mockMvc.perform(requestBuilder).
                andDo(MockMvcResultHandlers.print()).
                andExpect(MockMvcResultMatchers.status().is4xxClientError())
        ;
    }


    @Test
    public void updateRecipe_validRecipe_testMappingEntityToDto() throws Exception {
        Mockito.when(recipeService.updateRecipe(Mockito.any(), Mockito.anyString())).thenReturn(mockRecipe);

        RequestBuilder requestBuilder = buildUpdateRequest(createRecipeUpdateDTO());

        mockMvc.perform(requestBuilder).
                andDo(MockMvcResultHandlers.print()).
                andExpect(MockMvcResultMatchers.status().isOk()).
                andExpect(MockMvcResultMatchers.jsonPath("$.recipeName").value(RECIPE_NAME)).
                andExpect(MockMvcResultMatchers.jsonPath("$.recipeId").value(1)).
                andExpect(MockMvcResultMatchers.jsonPath("$.serves").value(3)).
                andExpect(MockMvcResultMatchers.jsonPath("prepTimeInMinutes").value(10)).
                andExpect(MockMvcResultMatchers.jsonPath("cookingTimeInMinutes").value(20)).
                andExpect(MockMvcResultMatchers.jsonPath("cuisine").value(CUISINE)).
                andExpect(MockMvcResultMatchers.jsonPath("course").value(COURSE));
        ;
    }


    private RecipeCreateDTO createRecipeCreateDTO() {
        return new RecipeCreateDTO(RECIPE_NAME,
                10, 20, true, true, 3,
                CUISINE, COURSE, LocalDateTime.now(),
                Collections.singletonList(new RecipeCreateDTO.IngredientsDTO(TOMATO, UNIT, 1)),
                Collections.singletonList(new RecipeCreateDTO.InstructionDTO(1, INSTRUCTION_TEXT)));

    }

    private RequestBuilder buildCreateRequest(RecipeCreateDTO recipeCreateDTO) throws JsonProcessingException {
        return MockMvcRequestBuilders
                .post("/recipe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recipeCreateDTO))
                .accept(MediaType.APPLICATION_JSON);
    }

    private RecipeUpdateDTO createRecipeUpdateDTO() {
        return new RecipeUpdateDTO(RECIPE_NAME,
                10, 20, true, true, 3,
                CUISINE, COURSE, LocalDateTime.now(),
                Collections.singletonList(new RecipeUpdateDTO.IngredientsDTO(TOMATO, UNIT, 1)),
                Collections.singletonList(new RecipeUpdateDTO.InstructionDTO(1, INSTRUCTION_TEXT)));

    }

    private RequestBuilder buildUpdateRequest(RecipeUpdateDTO recipeUpdateDTO) throws JsonProcessingException {
        return MockMvcRequestBuilders
                .put("/recipe/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recipeUpdateDTO))
                .accept(MediaType.APPLICATION_JSON);
    }

    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public ModelMapper bean() {
            return new ModelMapper();
        }
    }

}
