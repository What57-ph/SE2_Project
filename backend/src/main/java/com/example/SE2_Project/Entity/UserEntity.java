package com.example.SE2_Project.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.security.Timestamp;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;


}
