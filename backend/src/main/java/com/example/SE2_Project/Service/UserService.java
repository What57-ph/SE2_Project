package com.example.SE2_Project.Service;

import com.example.SE2_Project.Entity.UserEntity;
import com.example.SE2_Project.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public UserEntity getUserById(Long userId) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        return userOptional.orElseThrow(() -> new NoSuchElementException("Người dùng không tồn tại"));
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity createUser(UserEntity user) {
        user.setRole("user");
        user.setStatus(true);
        user.setTransactions(Collections.emptyList());
        return userRepository.save(user);
    }

    public UserEntity updateUser(Long id, UserEntity userDetails) {
        UserEntity user = getUserById(id);
        user.setName(userDetails.getName());
        user.setPassword(userDetails.getPassword());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        UserEntity user = getUserById(id);
        userRepository.delete(user);
    }
}
