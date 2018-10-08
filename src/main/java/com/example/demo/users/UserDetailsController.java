package com.example.demo.users;


import com.example.demo.users.auth.UserLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
public class UserDetailsController {

  @Autowired
  private UserLoginRepository userLoginRepository;


  @GetMapping
  public User getUserDetails(@Autowired Principal principal) {
    return userLoginRepository.findByUsername(principal.getName()).get().getUser();

  }

}
