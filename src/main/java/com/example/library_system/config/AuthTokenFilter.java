package com.example.library_system.config;

import com.example.library_system.service.JwtTokenService;
import com.example.library_system.service.UserDetailsServiceImpl;
import com.example.library_system.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    JwtTokenService tokenService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try{
            String jwt = parseJwt(request);

            //Kontrollera token
            if (jwt != null && jwtUtils.validateJwtToken(jwt)){
                    LOGGER.info("JWT token validated successfully");
            }

            //Kontrollera om token har gått ut
            Date expirationDate = jwtUtils.getExpirationFromToken(jwt);
            if(expirationDate.before(new Date())){
                LOGGER.warn("Token has expired for user: {}", jwtUtils.getUserNameFromJwtToken(jwt));
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token has expired. Please log in again");
                return;
            }

            //Kontrollera om token är blacklistad
            if(tokenService.isTokenBlacklisted(jwt)) {
                LOGGER.warn("Token is blacklisted for user: {}", jwtUtils.getUserNameFromJwtToken(jwt));
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token has been invalid. Please log in again");
                return;
            }


                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                LOGGER.info("Authenticated user: {}", username);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                LOGGER.info("User authorities: {}", userDetails.getAuthorities());


                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                       userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e){
            LOGGER.error("Cannot set user auth {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
        }


        private String parseJwt(HttpServletRequest request){
            String headerAuth = request.getHeader("Authorization");

            if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
                return headerAuth.substring(7);
            }
            return null;

        }



}
