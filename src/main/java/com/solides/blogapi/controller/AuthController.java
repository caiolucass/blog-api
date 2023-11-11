package com.solides.blogapi.controller;

import com.solides.blogapi.exception.AppException;
import com.solides.blogapi.exception.BlogApiException;
import com.solides.blogapi.model.User;
import com.solides.blogapi.model.role.Role;
import com.solides.blogapi.model.role.RoleName;
import com.solides.blogapi.payload.request.LoginRequest;
import com.solides.blogapi.payload.request.SignUpRequest;
import com.solides.blogapi.payload.response.ApiResponse;
import com.solides.blogapi.payload.response.JwtAuthenticationResponse;
import com.solides.blogapi.repository.RoleRepository;
import com.solides.blogapi.repository.UserRepository;
import com.solides.blogapi.security.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private static final String USER_ROLE_NOT_SET = "Usuário nao setado.";

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signIn")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signUp")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(signUpRequest.getUsername()))) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Username já cadastrado.");
        }

        if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequest.getEmail()))) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Email já cadastrado.");
        }

        String username = signUpRequest.getUsername().toLowerCase();

        String email = signUpRequest.getEmail().toLowerCase();

        String password = passwordEncoder.encode(signUpRequest.getPassword());

        User user = new User(username, email, password);

        List<Role> roles = new ArrayList<>();

        if (userRepository.count() == 0) {
            roles.add(roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));
            roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));
        } else {
            roles.add(roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));
        }

        user.setRoles(roles);

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{userId}")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(Boolean.TRUE, "Usuario registrado com sucesso."));
    }
}
