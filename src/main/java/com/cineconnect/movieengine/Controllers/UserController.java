package com.cineconnect.movieengine.Controllers;

import com.cineconnect.movieengine.Models.User;
import com.cineconnect.movieengine.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")

public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("addUser")
    public String addUser(@RequestBody User user){
        String response = userService.addUser(user);
        return response;
    }
}