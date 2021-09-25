package com.abnamro.nl.recipe.logic;

import com.abnamro.nl.recipe.entities.User;
import com.abnamro.nl.recipe.repos.UserRepository;
import com.abnamro.nl.recipe.utils.AuthUtils;
import com.abnamro.nl.recipe.utils.RecipeAppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

/**
 * UserService
 * Service containing user related business logic
 */
@Slf4j
@Component
public class UserService {

    @Value("${jwt.secret}")
    private String authenticationSigningSecret;

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void createUser(@Valid User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepo.findById(user.getUserName()).
                ifPresent(existingUser -> {
                    log.warn("Can not create user. already exist : {}", user.getUserName());
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, RecipeAppConstants.USER_ALREADY_EXIST);
                });

        userRepo.save(user);
    }


    public String signInUserAndGenerateJWT(@Valid User user) {
        authenticateUser(user);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
        return AuthUtils.generateJWT(userDetails.getUsername(),authenticationSigningSecret);
    }

    private void authenticateUser(User user) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
        } catch (DisabledException | BadCredentialsException ex) {
            log.debug("User login attempt failed for {} reason : {}", user.getUserName(), ex.getLocalizedMessage());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, RecipeAppConstants.USER_NOT_ALLOWED_OPERATION);
        }
    }


}
