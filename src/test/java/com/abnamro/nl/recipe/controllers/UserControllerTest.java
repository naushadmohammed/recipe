package com.abnamro.nl.recipe.controllers;


import com.abnamro.nl.recipe.dto.UserCreateDTO;
import com.abnamro.nl.recipe.logic.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class)
public class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @MockBean
    UserService userService;
    @MockBean
    UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSuccess() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO("TestUser12345", "TestPassword");
        Mockito.doNothing().when(userService).createUser(Mockito.any());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/signup").
                contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(userCreateDTO)).
                accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isCreated());

    }

    @Test
    public void testUser_InValidUserName() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO("test", "TestPassword");
        Mockito.doNothing().when(userService).createUser(Mockito.any());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/signup").
                contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(userCreateDTO)).
                accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }


    @Test
    public void testUser_InValidPassword() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO("TestUser12345", "short");
        Mockito.doNothing().when(userService).createUser(Mockito.any());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/signup").
                contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(userCreateDTO)).
                accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        ModelMapper modelMapper() {
            return new ModelMapper();
        }

        @Bean
        BCryptPasswordEncoder encoder() {
            return new BCryptPasswordEncoder();
        }
    }
}
