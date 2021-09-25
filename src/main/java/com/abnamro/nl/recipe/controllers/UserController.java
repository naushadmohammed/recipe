package com.abnamro.nl.recipe.controllers;

import com.abnamro.nl.recipe.dto.UserCreateDTO;
import com.abnamro.nl.recipe.entities.User;
import com.abnamro.nl.recipe.logic.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "users")
@AllArgsConstructor
/**
 * UserController
 * It allows user to be created and authenticated
 */
public class UserController {

    private ModelMapper modelMapper;
    private UserService userService;

    /**
     * User creation
     *
     * @param userDTO user details to create
     */
    @PostMapping("signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody @Valid UserCreateDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        userService.createUser(user);
    }

    /**
     * Authenticates user
     *
     * @param userCreateDTO user details to authenticate
     * @param httpResponse  on successful authentication header 'authorization' with bearer token which can be later used in all requests for auth
     */
    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.OK)
    public void signIn(@RequestBody @Valid UserCreateDTO userCreateDTO, HttpServletResponse httpResponse) {
        User user = modelMapper.map(userCreateDTO, User.class);
        String jwt = userService.signInUserAndGenerateJWT(user);
        httpResponse.setHeader("Access-Control-Expose-Headers", "Authorization, x-xsrf-token, Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, " +
                "Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
        httpResponse.addHeader("Authentication", "Bearer " + jwt);
    }
}
