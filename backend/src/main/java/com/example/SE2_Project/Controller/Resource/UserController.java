package com.example.SE2_Project.Controller.Resource;

import com.example.SE2_Project.Entity.UserEntity;
import com.example.SE2_Project.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUser(@PathVariable long id) {
        UserEntity user = userService.getUserById(id); // Lấy người dùng từ service
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
