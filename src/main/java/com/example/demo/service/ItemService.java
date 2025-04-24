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

    public List<Item> getItemsByBoxId(Long boxId) {
        return itemRepository.findByBoxId(boxId);
    }

    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    public Item getItemById(Long id) {
        return itemRepository.findById(id).orElse(null);
    }
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }
    public void deleteAllItems() {
        itemRepository.deleteAll();
    }
    public void deleteItemsByIds(List<Long> ids) {
        itemRepository.deleteAllById(ids);
    }
    
}
