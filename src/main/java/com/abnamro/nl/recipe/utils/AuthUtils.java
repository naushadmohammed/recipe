package com.abnamro.nl.recipe.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class AuthUtils {
    public static String generateJWT(String userName, String authenticationSigningSecret) {

        return Jwts.builder()
                .setSubject(userName)
                .setExpiration(new Date(System.currentTimeMillis() + 864_000_000))
                .signWith(SignatureAlgorithm.HS384, authenticationSigningSecret.getBytes()).compact();
    }

    public static String getUser(String token,String authenticationSigningSecret){
        return Jwts.parser().setSigningKey(authenticationSigningSecret.getBytes())
                .parseClaimsJws(token.replace("Bearer", ""))
                .getBody()
                .getSubject();
    }

}
