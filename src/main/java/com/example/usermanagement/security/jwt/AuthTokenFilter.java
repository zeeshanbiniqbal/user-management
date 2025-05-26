package com.example.usermanagement.security.jwt;


import com.example.usermanagement.service.AuthService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
	@Autowired
	private JwtUtils jwtUtils;


	// spring internal service class
	@Autowired
	private UserDetailsService userDetailsService;


	private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

	@Override
	protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, 
			jakarta.servlet.FilterChain filterChain) throws IOException, ServletException {

		String requestHeader = request.getHeader("Authorization");
		logger.info(" Header :  {}", requestHeader);
		String username = null;
		String token = null;
		if (requestHeader != null && requestHeader.startsWith("Bearer")) {

			token = requestHeader.substring(7);
			try {

				username = this.jwtUtils.getUsernameFromToken(token);

			} catch (IllegalArgumentException e) {
				logger.info("Illegal Argument while fetching the username !!");
				throw new ServletException("Illegal Argument while fetching the username !! "+e.getMessage());
			} catch (ExpiredJwtException e) {
				logger.info("Given jwt token is expired !!");
				throw new ServletException("Given jwt token is expired !! "+e.getMessage());
			} catch (MalformedJwtException e) {
				logger.info("Something changed in token or Invalid Token");
				throw new ServletException("Something changed in token or Invalid Token "+e.getMessage());
			} catch (Exception e) {
				throw new ServletException(" "+e.getMessage());
			}
		} else {
			logger.info("Invalid Header Value !! ");
		}

		setAuthentication(request, username, token);

		filterChain.doFilter(request, response);
	}

	// setting the authentication in context
	private void setAuthentication(HttpServletRequest request, String username, String token) {
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			//fetch user detail from username
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			Boolean validateToken = this.jwtUtils.validateToken(token, userDetails);
			if (validateToken) {

				//set the authentication
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} else {
				logger.info("Validation fails !!");
			}
		}
	}


}
