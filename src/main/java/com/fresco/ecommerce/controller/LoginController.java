package com.fresco.ecommerce.controller;

import com.fresco.ecommerce.dto.AuthRequest;
import com.fresco.ecommerce.service.JwtService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class LoginController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
      try {
          authenticationManager.authenticate(
                  new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
          );
      } catch (AuthenticationException ex) {
          return ResponseEntity.status(401).body("Unauthorized");
      }
      String token = jwtService.generateToken(authRequest.getUsername());
      Map<String, Object> response = new HashMap<>();
      response.put("accessToken", token);
      response.put("status", 200);
      return ResponseEntity.ok(response);
    }
}
