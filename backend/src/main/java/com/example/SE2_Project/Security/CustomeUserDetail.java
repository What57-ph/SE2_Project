package com.example.SE2_Project.Security;
import com.example.SE2_Project.Entity.UserEntity;
import com.example.SE2_Project.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;


import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class CustomeUserDetail implements  UserDetailsService {

    @Autowired
    private  UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if(StringUtils.isEmpty(username)){
            throw new UsernameNotFoundException("Username is empty");
        }
        UserEntity userEntity = userRepository.findByUsernameAndStatusIsFalse(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("Username not found");
        }

        // Tạo danh sách quyền từ chuỗi role (ví dụ: "ADMIN", "USER")
        Set<GrantedAuthority> grantedAuthorities = Collections.singleton(
                new SimpleGrantedAuthority("ROLE_" + userEntity.getRole().toUpperCase())
        );

        return new CustomUserDetails(userEntity, grantedAuthorities);
    }
    }



