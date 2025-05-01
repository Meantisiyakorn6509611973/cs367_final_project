// Service class that provides helper methods for basic CRUD operations on items.

package com.example.demo.service;

import com.example.demo.model.Item;
import com.example.demo.repository.ItemRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // Get items by the box ID they belong to
    public List<Item> getItemsByBoxId(Long boxId) {
        return itemRepository.findByBoxId(boxId);
    }

     // Save a new item or update an existing one
    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    // Get an item by its ID
    public Item getItemById(Long id) {
        return itemRepository.findById(id).orElse(null);
    }

    // Delete an item by its ID
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

     // Retrieve all items in the database
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    // Delete specific items by a list of IDs
    public void deleteItemsByIds(List<Long> ids) {
        itemRepository.deleteAllById(ids);
    }
    
}
