package com.example.usermanagement.service;

import com.example.usermanagement.dto.*;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.repository.UserRepository;
import com.example.usermanagement.security.MyConfig;
import com.example.usermanagement.security.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;


    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ApiResponse<?> register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            logger.info("requestId:"+" "+request.getRequestId()+" , endTime="+" "+request.getEndTime());
            return new ApiResponse<>(false, "Username already exists", null);
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        logger.info("requestId:"+" "+request.getRequestId()+" , endTime="+" "+request.getEndTime());
        return new ApiResponse<>(true, "User registered successfully", null);
    }

    public ApiResponse<AuthResponse> login(LoginRequest request) {
//      initialize userDetailsService bean first then call the loadUserByUsername
//        MyConfig myConfig = new MyConfig(userRepository);
//        myConfig.userDetailsService();
        //uncomment above codes after switching to an actual DB (i.e. DB != h2 DB)
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            logger.info("requestId:"+" "+request.getRequestId()+" , endTime="+" "+request.getEndTime());
            throw new RuntimeException("Invalid username or password");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtUtils.generateToken(userDetails);
        logger.info("requestId:"+" "+request.getRequestId()+" , endTime="+" "+request.getEndTime());
        return new ApiResponse<>(true, "Login successful", new AuthResponse(token));
    }

    public User findAndReturnUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
        //note that passwordEncoder does not have method/functionality to decrypt.
        //If decryption is needed, then use jasypt-spring-boot-starter
    }

    public User getUserDetails(Long id, GenericAttributes request) {
        logger.info("requestId:"+" "+request.getRequestId()+" , endTime="+" "+request.getEndTime());
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
    }

    public Page<User> findAll(Pageable pageable, GenericAttributes request) {
        logger.info("requestId:"+" "+request.getRequestId()+" , endTime="+" "+request.getEndTime());
        return userRepository.findAll(pageable);
    }

    public ApiResponse<?> updateUser(RegisterRequest request) {
        User user = getUserDetails(request.getId(), request);
        //user.setUsername(request.getUsername().isBlank()?user.getUsername():request.getUsername());
        user.setEmail(request.getEmail().isEmpty()?user.getEmail():request.getEmail());
        user.setPassword(request.getPassword().isBlank()?user.getPassword():passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        logger.info("requestId:"+" "+request.getRequestId()+" , endTime="+" "+request.getEndTime());
        return new ApiResponse<>(true, "User details update completed successfully", null);

    }

    public ApiResponse<?> deleteUserDetails(Long id, GenericAttributes request) {
        User user = getUserDetails(id, request);
        userRepository.delete(user);
        logger.info("requestId:"+" "+request.getRequestId()+" , endTime="+" "+request.getEndTime());
        return new ApiResponse<>(true, "User deleted successfully", null);
    }


}
