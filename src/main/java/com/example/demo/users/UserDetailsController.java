package com.example.demo.users;


import com.example.demo.users.auth.UserLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserDetailsController {

  @Autowired
  private UserLoginRepository userLoginRepository;

  @PostMapping
  public User getUserDetails(String username) {
    return userLoginRepository.findByUsername(username).get().getUser();

  }

}
