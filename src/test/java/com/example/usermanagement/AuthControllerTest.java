package com.example.usermanagement;

import com.example.usermanagement.controller.AuthController;
import com.example.usermanagement.dto.*;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;



class AuthControllerTest {

    private AuthController authController;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        authController = new AuthController(authService);
    }

    @Test
    void testRegister() {
        RegisterRequest request = new RegisterRequest("KUM.SOU", "kum.sou@wemail.com", "kum.sou");
        ApiResponse<?> expectedResponse = new ApiResponse<>(true, "User registered successfully", null);

        doReturn(expectedResponse).when(authService.register(any(RegisterRequest.class)));
        //when(authService.register(any(RegisterRequest.class))).thenReturn(expectedResponse);

        ResponseEntity<?> response = authController.register(request);

        assertEquals(expectedResponse, response.getBody());
        verify(authService, times(1)).register(any(RegisterRequest.class));
    }

    @Test
    void testLogin() {
        LoginRequest request = new LoginRequest("KUM.SOU", "kum.sou");
        ApiResponse<AuthResponse> expectedResponse = new ApiResponse<>(true, "Login successful", new AuthResponse("token"));

        when(authService.login(any(LoginRequest.class))).thenReturn(expectedResponse);

        ResponseEntity<?> response = authController.login(request);

        assertEquals(expectedResponse, response.getBody());
        verify(authService, times(1)).login(any(LoginRequest.class));
    }

    @Test
    void testGetUserDetails() {
        Long userId = 1L;
        User expectedUser = new User();
        expectedUser.setId(userId);
        expectedUser.setUsername("KUM.SOU");
        expectedUser.setEmail("kum.sou@wemail.com");

        when(authService.getUserDetails(eq(userId), any(GenericAttributes.class))).thenReturn(expectedUser);

        ResponseEntity<?> response = authController.getUserDetails(userId);

        assertEquals(expectedUser, response.getBody());
        verify(authService, times(1)).getUserDetails(eq(userId), any(GenericAttributes.class));
    }

    @Test
    void testGetUsers() {
        ApiResponse<?> expectedResponse = new ApiResponse<>(true, "Users fetched successfully", Collections.emptyList());

        doReturn(expectedResponse).when(authService.findAll(any(), any(GenericAttributes.class)));
        //when(authService.findAll(any(), any(GenericAttributes.class))).thenReturn(expectedResponse);

        ResponseEntity<?> response = authController.getUsers(0, 10);

        assertEquals(expectedResponse, response.getBody());
        verify(authService, times(1)).findAll(any(), any(GenericAttributes.class));
    }

    @Test
    void testUpdateUser() {
        RegisterRequest request = new RegisterRequest("KUM.SOU", "kum.sou@wemail.com", "kum.sou");
        request.setId(1L);
        ApiResponse<?> expectedResponse = new ApiResponse<>(true, "User details update completed successfully", null);

        doReturn(expectedResponse).when(authService.updateUser(any(RegisterRequest.class)));
        //when(authService.updateUser(any(RegisterRequest.class))).thenReturn(expectedResponse);

        ResponseEntity<?> response = authController.updateUser(request);

        assertEquals(expectedResponse, response.getBody());
        verify(authService, times(1)).updateUser(any(RegisterRequest.class));
    }

    @Test
    void testDeleteUserDetails() {
        Long userId = 1L;
        ApiResponse<?> expectedResponse = new ApiResponse<>(true, "User deleted successfully", null);

        doReturn(expectedResponse).when(authService.deleteUserDetails(eq(userId), any(GenericAttributes.class)));
        //when(authService.deleteUserDetails(eq(userId), any(GenericAttributes.class))).thenReturn(expectedResponse);

        ResponseEntity<?> response = authController.deleteUserDetails(userId);

        assertEquals(expectedResponse, response.getBody());
        verify(authService, times(1)).deleteUserDetails(eq(userId), any(GenericAttributes.class));
    }
}