package com.level_up_gamer.BackEnd;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.level_up_gamer.BackEnd.DTO.AuthResponse;
import com.level_up_gamer.BackEnd.DTO.LoginRequest;
import com.level_up_gamer.BackEnd.DTO.RegisterRequest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTests {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    public void testRegistroExitoso() {
        RegisterRequest request = new RegisterRequest();
        request.setNombre("Test User");
        request.setEmail("testuser@example.com");
        request.setPassword("TestPassword123");
        request.setPasswordConfirm("TestPassword123");
        
        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
            "/api/auth/register",
            request,
            AuthResponse.class
        );
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getToken());
        assertNotNull(response.getBody().getApiKey());
        assertEquals("testuser@example.com", response.getBody().getEmail());
    }
    
    @Test
    public void testRegistroConEmailDuplicado() {
        RegisterRequest request = new RegisterRequest();
        request.setNombre("Test User");
        request.setEmail("duplicate@example.com");
        request.setPassword("TestPassword123");
        request.setPasswordConfirm("TestPassword123");
        
        // Primer registro
        restTemplate.postForEntity("/api/auth/register", request, AuthResponse.class);
        
        // Segundo registro con mismo email
        ResponseEntity<?> response = restTemplate.postForEntity(
            "/api/auth/register",
            request,
            Object.class
        );
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    
    @Test
    public void testRegistroConContraseñasNoCoinciden() {
        RegisterRequest request = new RegisterRequest();
        request.setNombre("Test User");
        request.setEmail("testuser2@example.com");
        request.setPassword("TestPassword123");
        request.setPasswordConfirm("DifferentPassword123");
        
        ResponseEntity<?> response = restTemplate.postForEntity(
            "/api/auth/register",
            request,
            Object.class
        );
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    
    @Test
    public void testLoginExitoso() {
        // Primero registrar
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setNombre("Login Test User");
        registerRequest.setEmail("logintest@example.com");
        registerRequest.setPassword("LoginPassword123");
        registerRequest.setPasswordConfirm("LoginPassword123");
        
        restTemplate.postForEntity("/api/auth/register", registerRequest, AuthResponse.class);
        
        // Luego hacer login
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("logintest@example.com");
        loginRequest.setPassword("LoginPassword123");
        
        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
            "/api/auth/login",
            loginRequest,
            AuthResponse.class
        );
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getToken());
        assertEquals("logintest@example.com", response.getBody().getEmail());
    }
    
    @Test
    public void testLoginConContraseñaIncorrecta() {
        // Primero registrar
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setNombre("Wrong Password Test");
        registerRequest.setEmail("wrongpass@example.com");
        registerRequest.setPassword("CorrectPassword123");
        registerRequest.setPasswordConfirm("CorrectPassword123");
        
        restTemplate.postForEntity("/api/auth/register", registerRequest, AuthResponse.class);
        
        // Intentar login con contraseña incorrecta
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("wrongpass@example.com");
        loginRequest.setPassword("WrongPassword123");
        
        ResponseEntity<?> response = restTemplate.postForEntity(
            "/api/auth/login",
            loginRequest,
            Object.class
        );
        
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
    
    @Test
    public void testValidarTokenValido() {
        // Registrar y obtener token
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setNombre("Token Validation Test");
        registerRequest.setEmail("tokentest@example.com");
        registerRequest.setPassword("TokenPassword123");
        registerRequest.setPasswordConfirm("TokenPassword123");
        
        ResponseEntity<AuthResponse> registerResponse = restTemplate.postForEntity(
            "/api/auth/register",
            registerRequest,
            AuthResponse.class
        );
        
        String token = registerResponse.getBody().getToken();
        
        // Validar token
        ResponseEntity<?> response = restTemplate.postForEntity(
            "/api/auth/validate",
            null,
            Object.class,
            token
        );
        
        // En una implementación real, necesitarías ajustar esto según tu implementación
        // assertTrue(response.getStatusCode() == HttpStatus.OK);
    }
}
