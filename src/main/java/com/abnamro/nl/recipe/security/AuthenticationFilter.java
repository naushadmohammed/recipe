package com.abnamro.nl.recipe.security;

import com.abnamro.nl.recipe.dto.UserCreateDTO;
import com.abnamro.nl.recipe.utils.AuthUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * AuthenticationFilter
 * Extend UsernamePasswordAuthenticationFilter and
 * overrides attemptAuthentication to use custom UserCreateDTO in request
 * overrides successfulAuthentication to generate custom header in response 'Authorization' containing JWT for further requests
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private String authenticationSigningSecret;
    private AuthenticationManager authenticationManager;

    /**
     * public constructor
     * @param authenticationManager Spring authentication manager
     * @param authenticationSigningSecret Secret used for JWT signing
     */
    public AuthenticationFilter(AuthenticationManager authenticationManager, String authenticationSigningSecret) {
        this.authenticationManager = authenticationManager;
        this.authenticationSigningSecret = authenticationSigningSecret;
        setFilterProcessesUrl("/login");
    }

    /**
     * Adds custom header on successful authentication
     * @param request http request
     * @param response http response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UserCreateDTO user = new ObjectMapper().readValue(request.getInputStream(), UserCreateDTO.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
        } catch (IOException ioe) {
            throw new RuntimeException("Could not read request" + ioe);

        }
    }


    /**
     * Adds custom header on successful authentication
     * @param request http request
     * @param response http response
     * @param chain filter chains
     * @param authResult auth result
     * @throws IOException may throw while trying to serialize/deserialize http request response
     * @throws ServletException  may throw while trying to process http request response
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        String token = AuthUtils.generateJWT(((User) authResult.getPrincipal()).getUsername(), authenticationSigningSecret);
        response.setHeader("Access-Control-Expose-Headers", "Authorization, x-xsrf-token, Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, " +
                "Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
        response.addHeader("Authorization", "Bearer " + token);

    }
}
