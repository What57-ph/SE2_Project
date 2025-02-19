package com.example.SE2_Project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SE2_Project.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
