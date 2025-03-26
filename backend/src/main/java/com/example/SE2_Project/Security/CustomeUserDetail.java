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
import java.util.Optional;
import java.util.Set;

@Service
public class CustomeUserDetail implements  UserDetailsService {
    @Autowired
    private  UserRepository userRepository;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Check if username is empty
        if (StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException("Username is empty");
        }

        // Fetch user entity based on username (status is false means active)
        UserEntity userEntity = userRepository.findByUsernameAndStatusIsFalse(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Create authorities list from user roles
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority( userEntity.getRole().toUpperCase()));

        // Return UserDetails with username, password, and authorities
        return new User(userEntity.getUsername(), userEntity.getPassword(), grantedAuthorities);
    }
}


