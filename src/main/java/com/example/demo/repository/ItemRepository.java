// Repository interface for accessing and managing Item entities using Spring Data JPA.

package com.example.demo.repository;

import com.example.demo.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    
    // Finds all items that belong to the box with the given box ID
    List<Item> findByBoxId(Long boxId);
}
