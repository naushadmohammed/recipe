package com.abnamro.nl.recipe.utils;

import com.abnamro.nl.recipe.dto.RecipeGetDTO;
import com.abnamro.nl.recipe.dto.RecipeListDTO;
import com.abnamro.nl.recipe.entities.Recipe;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableCaching
public class GlobalConfig {

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper;
        modelMapper = new ModelMapper();

        //Making sure user maps properly to Get All request
        modelMapper.addMappings(new PropertyMap<Recipe, RecipeListDTO>() {
            @Override
            protected void configure() {
                map().setCreatedBy(source.getCreatedBy().getUserName());
            }
        });

        //Making sure user maps properly to Get detailed recipe request
        modelMapper.addMappings(new PropertyMap<Recipe, RecipeGetDTO>() {
            @Override
            protected void configure() {
                map().setCreatedBy(source.getCreatedBy().getUserName());
            }
        });

        return modelMapper;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
