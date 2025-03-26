package com.example.SE2_Project.Controller.Resource;

import com.example.SE2_Project.Dto.UserDto;
import com.example.SE2_Project.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable long id) {
        UserDto userDto = userService.convertToDto(userService.getUserById(id)); // Chuyển đổi từ Entity sang DTO
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/getAll/")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers().stream()
                .map(user -> userService.convertToDto(user)) // Chuyển đổi các UserEntity sang UserDto
                .toList();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/create/")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        UserDto newUser = userService.createUser(userDto); // Tạo người dùng mới từ DTO
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(id, userDto); // Cập nhật người dùng từ DTO
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
