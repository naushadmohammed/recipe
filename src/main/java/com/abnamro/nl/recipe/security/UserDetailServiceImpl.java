package com.abnamro.nl.recipe.security;

import com.abnamro.nl.recipe.entities.User;
import com.abnamro.nl.recipe.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * UserDetailServiceImpl
 * Overrides loadUserByUsername to use custom user entity
 */
@Component
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     *
     * @param userName name of user to authenticate
     * @return UserDetails containing  User object with principal details
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findById(userName).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException(userName);
        }

        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), Collections.emptyList());
    }
}
