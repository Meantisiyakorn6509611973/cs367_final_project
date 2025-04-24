package com.example.demo.repository;

import com.example.demo.model.Box;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BoxRepository extends JpaRepository<Box, Long> {
    List<Box> findByBoxName(String boxName);
}
