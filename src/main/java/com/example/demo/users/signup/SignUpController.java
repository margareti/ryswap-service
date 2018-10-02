package com.example.demo.users.signup;

import java.net.URI;
import java.util.Collections;

import javax.validation.Valid;

import com.example.demo.OperationResult;
import com.example.demo.users.User;
import com.example.demo.users.UserRepository;
import com.example.demo.users.auth.RoleRepository;
import com.example.demo.users.auth.UserLogin;
import com.example.demo.users.auth.UserLoginRepository;
import com.example.demo.users.auth.UserRole;
import com.example.demo.users.auth.UserRoleName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api")
public class SignUpController {

  @Autowired 
  private UserLoginRepository userLoginRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired 
  private UserRepository userRepository;

  @Autowired 
  private PasswordEncoder passwordEncoder;
  
  
  @PostMapping("/registerUser")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
      if (userLoginRepository.existsByUsername(signUpRequest.getEmail())) {
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
      userLogin.setUsername(signUpRequest.getEmail());
      userLogin.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
      userLoginRepository.save(userLogin);

      URI location = ServletUriComponentsBuilder
              .fromCurrentContextPath().path("/users/{username}")
              .buildAndExpand(userLogin.getUsername()).toUri();

      return OperationResult.succes("User registered successfully").created(location).build();
  }
}