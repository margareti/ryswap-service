package com.example.demo.users.login;

import com.example.demo.OperationResult;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.users.User;
import com.example.demo.users.UserRepository;
import com.example.demo.users.auth.*;
import com.example.demo.users.signup.SignUpRequest;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserLoginRepository userLoginRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        String jwt = getJwtToken(loginRequest);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    private String getJwtToken(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return tokenProvider.generateToken(authentication);
    }

    @PostMapping("/registerUser")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userLoginRepository.existsByUsername(signUpRequest.getUsername())) {
            return OperationResult.error("", "Username already taken!");
        }

        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getEmail());
        UserRole userUserRole = roleRepository.findByName(UserRoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("User UserRole not set."));
        user.setUserRoles(Collections.singleton(userUserRole));
        userRepository.save(user);

        UserLogin userLogin = new UserLogin();
        userLogin.setUser(user);
        userLogin.setUsername(signUpRequest.getUsername());
        userLogin.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        userLoginRepository.save(userLogin);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(userLogin.getUsername()).toUri();

        return OperationResult.succes("User registered successfully").created(location).build();
    }

}
