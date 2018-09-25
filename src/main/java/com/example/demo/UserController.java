package com.example.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class UserController {

  @RequestMapping({ "/user", "/me" })
  public Map<String, Principal> user(Principal principal) {
    Map<String, Principal> map = new LinkedHashMap<>();
    map.put("user", principal);
    return map;
  }
}
