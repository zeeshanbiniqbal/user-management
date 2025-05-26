package com.example.usermanagement.security;



import com.example.usermanagement.entity.User;
import com.example.usermanagement.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.List;


@Configuration
public class MyConfig {

    private final UserRepository userRepository;

    public MyConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        //this logic will work with an actual DB (i.e. DB != h2 DB)
//        List<UserDetails> usersDetails = new ArrayList<>();
//        for (User eachUser : userRepository.findAll()) {
//            UserDetails userDetail = org.springframework.security.core.userdetails.User.builder()
//                    .username(eachUser.getUsername())
//                    .password(eachUser.getPassword())
//                    .roles("ADMIN").build();
//            usersDetails.add(userDetail);
//        }
//        return new InMemoryUserDetailsManager(usersDetails);

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder().username("KUM.SOU")
                .password(passwordEncoder().encode("kum.sou")).roles("ADMIN").build();

        UserDetails userDetails1 = org.springframework.security.core.userdetails.User.builder().username("Zeeshan")
                .password(passwordEncoder().encode("zeeshan")).roles("ADMIN").build();
        return new InMemoryUserDetailsManager(userDetails, userDetails1);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }
}
