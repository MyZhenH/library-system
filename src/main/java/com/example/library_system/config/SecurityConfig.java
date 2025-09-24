package com.example.library_system.config;

import com.example.library_system.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors().disable()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/html/**", "/css/**", "/js/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/home", "/books/**", "/authors/**").permitAll()
                        .requestMatchers("/users/register").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/login").permitAll()

                        //Admin
                        .requestMatchers(HttpMethod.POST, "/authors").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/books").hasRole("ADMIN")
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/users").hasRole("ADMIN")


                        //User
                        .requestMatchers("/loan").authenticated()
                        .requestMatchers("/loans/{loanId}/return").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/user").hasRole("USER")

                        .anyRequest().authenticated()

                );

                //Om man har frontEnd
                //.formLogin(form -> form
                        //.loginPage("/login")
                        //.defaultSuccessUrl("/home")
                        //.failureUrl("login?error")
                        //.permitAll()
                //)
                //.logout(logout-> logout
                          //.logoutSuccessUrl("/login")
                          //.permitAll()
                //);

                http.authenticationProvider(authenticationProvider());
                http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

                return http.build();

    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration autConfig) throws Exception{
      return autConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;

    }







}