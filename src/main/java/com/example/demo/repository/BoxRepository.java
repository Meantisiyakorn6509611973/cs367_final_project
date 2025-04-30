// Repository interface for accessing and managing Box entities using Spring Data JPA.

package com.example.demo.repository;

import com.example.demo.model.Box;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BoxRepository extends JpaRepository<Box, Long> {
    
    // Finds all boxes with the specified box name
    List<Box> findByBoxName(String boxName);
}
