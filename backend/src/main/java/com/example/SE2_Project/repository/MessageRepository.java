package com.example.SE2_Project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SE2_Project.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
