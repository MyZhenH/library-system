package com.example.library_system.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

//Skapa och validera våra JWT token
@Component
public class JwtUtils {

    public static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwtSecret:mySecretKey}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;


    public String generateJwtToken(Authentication authentication){
      UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("roles", userPrincipal.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }


    public String getUserNameFromJwtToken(String token){
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    public Date getExpirationFromToken(String token){
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }


    public boolean validateJwtToken(String authToken){

        try{
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;

        }catch (SignatureException e){
            LOGGER.error("Invalid JWT sign {}", e.getMessage());

        }catch (MalformedJwtException e){
            LOGGER.error("Invalid JWT token {}", e.getMessage());

        }catch (ExpiredJwtException e){
            LOGGER.error("JWT token is expired {}", e.getMessage());

        }catch (UnsupportedJwtException e){
            LOGGER.error("JWT token is unsupported {}", e.getMessage());

        }catch (IllegalArgumentException e){
            LOGGER.error("JWT claims string is empty {}", e.getMessage());
        }
        return false;
    }



}
