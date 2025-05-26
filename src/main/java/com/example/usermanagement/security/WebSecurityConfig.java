package com.example.usermanagement.security;

import com.example.usermanagement.security.jwt.AuthEntryPointJwt;
import com.example.usermanagement.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@SuppressWarnings("ALL")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity( prePostEnabled = true)
public class WebSecurityConfig  {


	@Autowired
	private AuthEntryPointJwt point;

	@Autowired
	private AuthTokenFilter filter;


	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable()) // disabling Cross-Site Request Forgery - protects against unauthorized actions from a user the server trusts
				.cors(cors -> cors.disable())  // disabling Cross-Origin Resource Sharing - microservices comms won't work now
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			    .and().authorizeRequests().requestMatchers("/api/auth/**").permitAll() //use authorizeHttpRequests
			    .requestMatchers("/api/test/**").permitAll()
			    .requestMatchers("/error").permitAll()
				.anyRequest()
				.authenticated()
				.and().exceptionHandling(ex -> ex.authenticationEntryPoint(point))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); //STATELESS policy ensures no saving anything on server

		http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

}
