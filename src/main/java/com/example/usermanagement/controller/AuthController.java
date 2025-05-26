package com.example.usermanagement.controller;

import com.example.usermanagement.dto.*;
import com.example.usermanagement.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            request.setRequestId(Math.abs(UUID.randomUUID().getMostSignificantBits()));
            logger.info(request.getStartTime()+request.toString());
            return ResponseEntity.ok(authService.register(request));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        request.setRequestId(Math.abs(UUID.randomUUID().getMostSignificantBits()));
        logger.info(request.getStartTime()+request.toString());
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/getUserDetails/{id}")
    public ResponseEntity<?> getUserDetails(@PathVariable Long id) {
        GenericAttributes request = new GenericAttributes();
        request.setRequestId(Math.abs(UUID.randomUUID().getMostSignificantBits()));
        logger.info(request.getStartTime()+request.toString());
        return ResponseEntity.ok(authService.getUserDetails(id, request));
    }

    @GetMapping("/getUsers")
    public ResponseEntity<?> getUsers(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        Sort sort = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        GenericAttributes request = new GenericAttributes();
        request.setRequestId(Math.abs(UUID.randomUUID().getMostSignificantBits()));
        logger.info(request.getStartTime()+request.toString());
        return ResponseEntity.ok(authService.findAll(pageable, request));
    }

    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody RegisterRequest request) {
        request.setRequestId(Math.abs(UUID.randomUUID().getMostSignificantBits()));
        logger.info(request.getStartTime()+request.toString());
        return ResponseEntity.ok(authService.updateUser(request));
    }

    @DeleteMapping("/deleteUserDetails/{id}")
    public ResponseEntity<?> deleteUserDetails(@PathVariable  Long id) {
        GenericAttributes request = new GenericAttributes();
        request.setRequestId(Math.abs(UUID.randomUUID().getMostSignificantBits()));
        logger.info(request.getStartTime()+request.toString());
        return ResponseEntity.ok(authService.deleteUserDetails(id, request));
    }
}
