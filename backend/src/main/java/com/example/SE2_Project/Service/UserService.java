package com.example.SE2_Project.Service;

import com.example.SE2_Project.Dto.UserDto;
import com.example.SE2_Project.Entity.UserEntity;
import com.example.SE2_Project.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Đối tượng BCryptPasswordEncoder để mã hóa mật khẩu
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Chuyển đổi từ UserDto sang UserEntity
    public UserEntity convertToEntity(UserDto userDto) {
        UserEntity user = new UserEntity();
        user.setUsername(userDto.getUsername());
        user.setName(userDto.getName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword())); // Mã hóa mật khẩu
        user.setRole("USER");
        user.setStatus(true);
        user.setTransactions(Collections.emptyList());
        return user;
    }

    // Chuyển đổi từ UserEntity sang UserDto
    public UserDto convertToDto(UserEntity userEntity) {
        UserDto userDto = new UserDto();
        userDto.setUsername(userEntity.getUsername());
        userDto.setName(userEntity.getName());
        userDto.setPassword(userEntity.getPassword()); // Lưu mật khẩu đã mã hóa
        return userDto;
    }

    public UserEntity getUserById(Long userId) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        return userOptional.orElseThrow(() -> new NoSuchElementException("Người dùng không tồn tại"));
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserDto createUser(UserDto userDto) {
        UserEntity userEntity = convertToEntity(userDto); // Chuyển đổi DTO sang Entity
        userRepository.save(userEntity);
        return convertToDto(userEntity); // Trả về DTO sau khi lưu
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        UserEntity user = getUserById(id);
        user.setName(userDto.getName());
        if (userDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword())); // Mã hóa mật khẩu mới
        }
        userRepository.save(user);
        return convertToDto(user); // Trả về DTO sau khi cập nhật
    }

    public void deleteUser(Long id) {
        UserEntity user = getUserById(id);
        userRepository.delete(user);
    }
}
