package com.example.SE2_Project.Repository;

import com.example.SE2_Project.Entity.SearchIndexEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchIndexRepository extends JpaRepository<SearchIndexEntity, Long> {
}
