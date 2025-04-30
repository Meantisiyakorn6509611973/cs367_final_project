package com.example.demo.service;

import com.example.demo.model.Box;
import com.example.demo.model.Item;
import com.example.demo.repository.BoxRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.dto.BoxPurchaseRequest;
import com.example.demo.dto.ItemQuantityUpdateRequest;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BoxService {

    private final BoxRepository boxRepository;
    private final ItemRepository itemRepository;

    public BoxService(BoxRepository boxRepository, ItemRepository itemRepository) {
        this.boxRepository = boxRepository;
        this.itemRepository = itemRepository;
    }

    public List<Box> getAllBoxes() {
        return boxRepository.findAll();
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Box saveBox(Box box) {
        List<Box> existingBoxes = boxRepository.findByBoxName(box.getBoxName());
        if (!existingBoxes.isEmpty()) {
            throw new IllegalStateException("Box name '" + box.getBoxName() + "' already exists.");
        }
    
        Box savedBox = boxRepository.save(box);
        if (box.getItems() != null) {
            for (Item item : box.getItems()) {
                item.setBox(savedBox);
            }
            itemRepository.saveAll(box.getItems());
        }
        return savedBox;
    }

    public List<Box> getBoxesByBoxName(String boxName) {
        return boxRepository.findByBoxName(boxName);
    }

    public List<Item> getRandomItemsFromBoxName(String boxName, int numberOfItems) {
        List<Box> boxes = boxRepository.findByBoxName(boxName);
        if (boxes.isEmpty()) {
            throw new IllegalStateException("Error: No box found with name: " + boxName);
        }
    
        Box box = boxes.get(0);
        List<Item> availableItems = box.getItems().stream()
                .filter(item -> item.getItemAmount() > 0)
                .collect(Collectors.toList());
    
        if (availableItems.size() < numberOfItems) {
            throw new IllegalStateException("Error: Not enough item types in stock for box: " + boxName);
        }
    
        // Shuffle and choose candidates
        List<Item> shuffled = new ArrayList<>(availableItems);
        Collections.shuffle(shuffled);
        List<Item> selectedItems = shuffled.subList(0, numberOfItems);
    
        // Verify all selected items have enough quantity
        for (Item item : selectedItems) {
            if (item.getItemAmount() <= 0) {
                throw new IllegalStateException("Error: Item " + item.getItemName() + " is out of stock.");
            }
        }
    
        // All good — now deduct amounts
        for (Item item : selectedItems) {
            item.setItemAmount(item.getItemAmount() - 1);
        }
    
        itemRepository.saveAll(selectedItems);
        return selectedItems;
    }    

    public List<Item> purchaseBoxesByBoxName(String boxName, int quantity) {
        List<Box> boxes = boxRepository.findByBoxName(boxName);
        if (boxes.isEmpty()) {
            throw new IllegalStateException("Error: No box found with name: " + boxName);
        }
    
        Box box = boxes.get(0);
        List<Item> availableItems = box.getItems().stream()
                .filter(item -> item.getItemAmount() > 0)
                .collect(Collectors.toList());
    
        if (availableItems.size() < quantity) {
            throw new IllegalStateException("Error: Not enough different item types available for requested boxes.");
        }
    
        // Check if each item can support reduction without modifying anything yet
        List<Item> selectedItems = new ArrayList<>();
        List<Item> tempList = new ArrayList<>(availableItems);
        Collections.shuffle(tempList);
    
        for (int i = 0; i < quantity; i++) {
            Item item = tempList.get(i);
            if (item.getItemAmount() < 1) {
                throw new IllegalStateException("Error: Item " + item.getItemName() + " has insufficient stock.");
            }
            selectedItems.add(item);
        }
    
        // All checks passed, now deduct quantities and save
        for (Item item : selectedItems) {
            item.setItemAmount(item.getItemAmount() - 1);
        }
        itemRepository.saveAll(selectedItems);
    
        return selectedItems;
    }    

    public List<Item> purchaseMultipleBoxes(List<BoxPurchaseRequest> boxRequests) {
        List<Item> allItems = new ArrayList<>();
        for (BoxPurchaseRequest request : boxRequests) {
            allItems.addAll(purchaseBoxesByBoxName(request.getBoxName(), request.getQuantity()));
        }
        return allItems;
    }

    public Box addItemsToExistingBoxAndReturnFullBox(Long boxId, List<Item> items) {
        Optional<Box> boxOptional = boxRepository.findById(boxId);
        if (boxOptional.isEmpty()) {
            throw new IllegalArgumentException("Box with ID " + boxId + " not found.");
        }

        Box box = boxOptional.get();
        for (Item item : items) {
            item.setBox(box);
        }

        itemRepository.saveAll(items);
        return boxRepository.findById(boxId).orElseThrow();
    }

    public void deleteBoxById(Long id) {
        if (!boxRepository.existsById(id)) {
            throw new IllegalArgumentException("Box with ID " + id + " does not exist.");
        }
        boxRepository.deleteById(id);
    }

    public Box getBoxById(Long id) {
        return boxRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Box with ID " + id + " not found."));
    }

    public Map<String, Object> deleteItemsAndReturnInfo(List<Long> itemIds) {
        List<Item> deletedItems = itemRepository.findAllById(itemIds);
        itemRepository.deleteAllById(itemIds);
        List<Item> remainingItems = itemRepository.findAll();

        return Map.of(
                "message", "Items deleted successfully.",
                "deletedItems", deletedItems,
                "remainingItems", remainingItems
        );
    }

    public Map<String, Object> handleItemAction(ItemQuantityUpdateRequest req) {
        Long itemId = req.getItemId();
        String action = req.getAction();
    
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item with ID " + itemId + " not found."));
    
        Map<String, Object> boxInfo = Map.of(
                "boxId", item.getBox().getId(),
                "boxName", item.getBox().getBoxName(),
                "boxPrice", item.getBox().getBoxPrice()
        );
    
        if ("delete".equalsIgnoreCase(action)) {
            itemRepository.deleteById(itemId);
            return Map.of(
                    "action", "deleted",
                    "itemId", itemId,
                    "itemName", item.getItemName(),
                    "box", boxInfo
            );
        }
    
        if ("update".equalsIgnoreCase(action)) {
            int delta = req.getDeltaAmount();
            int before = item.getItemAmount();
            int after = before + delta;
    
            if (after < 0) {
                throw new IllegalArgumentException("Item quantity can't go below zero.");
            }
    
            item.setItemAmount(after);
            itemRepository.save(item);
    
            return Map.of(
                    "action", "updated",
                    "itemId", item.getItemId(),
                    "itemName", item.getItemName(),
                    "beforeAmount", before,
                    "delta", delta,
                    "afterAmount", after,
                    "box", boxInfo
            );
        }
    
        throw new IllegalArgumentException("Invalid action: " + action);
    }
    
}
