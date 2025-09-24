package com.example.library_system.service;

import com.example.library_system.utils.JwtUtils;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;



@Service
public class JwtTokenService {


    private final Set<String> blacklistedTokens = new ConcurrentHashMap<>().newKeySet();

    //Invalidera token genom att lägga token i blacklist
    public void blacklistToken(String jwt){
        blacklistedTokens.add(jwt);
    }

    public boolean isTokenBlacklisted(String jwt){
        return blacklistedTokens.contains(jwt);
    }

    public String extractTokenFromHeader(String authHeader){
        if(authHeader != null && authHeader.startsWith("Bearer")){
            return authHeader.substring(7);
        }
        return null;
    }


}
