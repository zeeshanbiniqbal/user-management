package com.example.usermanagement.controller;

import com.example.usermanagement.dto.ApiResponse;
import com.example.usermanagement.dto.AuthResponse;
import com.example.usermanagement.dto.LoginRequest;
import com.example.usermanagement.dto.RegisterRequest;
import com.example.usermanagement.service.AuthService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/getUserDetails/{id}")
    public ResponseEntity<?> getUserDetails(@PathVariable Long id) {
        return ResponseEntity.ok(authService.getUserDetails(id));
    }

    @GetMapping("/getUsers")
    public ResponseEntity<?> getUsers(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        Sort sort = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(authService.findAll(pageable));
    }

    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.updateUser(request));
    }

    @DeleteMapping("/deleteUserDetails/{id}")
    public ResponseEntity<?> deleteUserDetails(@PathVariable  Long id) {
        return ResponseEntity.ok(authService.deleteUserDetails(id));
    }
}
