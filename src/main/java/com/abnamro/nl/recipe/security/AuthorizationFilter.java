package com.abnamro.nl.recipe.security;

import com.abnamro.nl.recipe.utils.AuthUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * AuthenticationFilter
 *  overrides doFilterInternal to get bearer token and adds  authenticationToken to security context
 */
public class AuthorizationFilter extends BasicAuthenticationFilter {

    private String authenticationSigningSecret;

    /**
     * public constructor
     * @param authenticationManager
     * @param authenticationSigningSecret
     */
    public AuthorizationFilter(AuthenticationManager authenticationManager, String authenticationSigningSecret) {
        super(authenticationManager);
        this.authenticationSigningSecret = authenticationSigningSecret;
    }

    /**
     * overrides doFilterInternal to get bearer token and adds  authenticationToken to security context
     * @param request http request
     * @param response http response
     * @param filterChain Spring filter chain
     * @throws IOException may throw while trying to serialize/deserialize http request response
     * @throws ServletException may throw while trying to process http request response
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }

    /**
     * Gets user from Bearer token and generates UsernamePasswordAuthenticationToken
     * @param request http request
     * @return UsernamePasswordAuthenticationToken
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null) {
            String user = AuthUtils.getUser(token, authenticationSigningSecret);
            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}
