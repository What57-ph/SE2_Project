package com.example.SE2_Project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SE2_Project.model.Notification;
import org.springframework.stereotype.Repository;

@Repository

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
