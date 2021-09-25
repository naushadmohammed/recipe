package com.abnamro.nl.recipe.logic;

import com.abnamro.nl.recipe.entities.User;
import com.abnamro.nl.recipe.repos.UserRepository;
import com.abnamro.nl.recipe.utils.RecipeAppConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    @MockBean
    UserRepository userRepository;

    @MockBean
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    UserService userService;

    User user = new User("TestUser12345", "Password", null);

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_UserIsValid_AlreadyExist() {
        Mockito.when(userRepository.findById(Mockito.anyString())).
                thenReturn(Optional.of(user));
        ResponseStatusException responseStatusException =
                Assert.assertThrows(ResponseStatusException.class, () -> userService.createUser(user));
        Assert.assertEquals(HttpStatus.UNAUTHORIZED, responseStatusException.getStatus());
        Assert.assertEquals(RecipeAppConstants.USER_ALREADY_EXIST, responseStatusException.getReason());
    }

    @Test
    public void test_UserValid_IsNewUser() {
        Mockito.when(userRepository.findById(Mockito.anyString())).
                thenReturn(Optional.empty());
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        userService.createUser(user);
        Mockito.verify(userRepository,Mockito.times(1)).findById(user.getUserName());
        Mockito.verify(userRepository,Mockito.times(1)).save(user);
    }

}
