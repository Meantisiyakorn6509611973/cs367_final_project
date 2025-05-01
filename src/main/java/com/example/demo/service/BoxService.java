// Service layer for handling box operations including creation, purchase, and item stock management.
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

    // Fetch all boxes
    public List<Box> getAllBoxes() {
        return boxRepository.findAll();
    }

    // Fetch all items
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    // Save a new box if the name is unique, along with its items
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

     // Find boxes by name
    public List<Box> getBoxesByBoxName(String boxName) {
        return boxRepository.findByBoxName(boxName);
    }

     // Select random items from a box and reduce their quantities (used in purchasing logic)
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

    // Purchase boxes by name, ensuring availability and updating stock
    public List<Item> purchaseBoxesByBoxName(String boxName, int quantity) {
        // Step 1: Validate that quantity is a positive number
        if (quantity <= 0) {
            throw new IllegalStateException("Quantity must be greater than 0.");
        }
        
        // Step 2: Find the box by name
        List<Box> boxes = boxRepository.findByBoxName(boxName);
        if (boxes.isEmpty()) {
            throw new IllegalStateException("Error: No box found with name: " + boxName);
        }
    
        Box box = boxes.get(0);

         // Step 3: Get available items from the box
        List<Item> availableItems = box.getItems().stream()
                .filter(item -> item.getItemAmount() > 0)
                .collect(Collectors.toList());
    
        // Step 4: Validate that enough items are available
        if (availableItems.size() < quantity) {
            throw new IllegalStateException("Error: Not enough different item types available for requested boxes.");
        }
    
        // Step 5: Select items to be reduced
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
    
        // Step 6: Deduct item amounts only after validation
        for (Item item : selectedItems) {
            item.setItemAmount(item.getItemAmount() - 1);
        }
        itemRepository.saveAll(selectedItems);
    
        return selectedItems;
    }    

    public List<Item> purchaseMultipleBoxes(List<BoxPurchaseRequest> boxRequests) {
        List<Item> allSelectedItems = new ArrayList<>();
    
        // Phase 1: Validate everything first
        for (BoxPurchaseRequest request : boxRequests) {
            String boxName = request.getBoxName();
            int quantity = request.getQuantity();
    
            if (quantity <= 0) {
                throw new IllegalStateException("Quantity must be greater than 0 for box: " + boxName);
            }
    
            List<Box> boxes = boxRepository.findByBoxName(boxName);
            if (boxes.isEmpty()) {
                throw new IllegalStateException("Error: No box found with name: " + boxName);
            }
    
            Box box = boxes.get(0);
            List<Item> availableItems = box.getItems().stream()
                .filter(item -> item.getItemAmount() > 0)
                .collect(Collectors.toList());
    
            int totalAvailable = availableItems.stream()
                .mapToInt(Item::getItemAmount)
                .sum();
    
            if (totalAvailable < quantity) {
                throw new IllegalStateException("Error: Not enough total stock to fulfill request for box: " + boxName);
            }
    
            // Randomly select items based on quantity and available stock
            List<Item> selectedItems = new ArrayList<>();
            Random random = new Random();
    
            while (selectedItems.size() < quantity) {
                Item randomItem = availableItems.get(random.nextInt(availableItems.size()));
    
                if (randomItem.getItemAmount() > 0) {
                    selectedItems.add(randomItem);
                    randomItem.setItemAmount(randomItem.getItemAmount() - 1);
                }
            }
    
            allSelectedItems.addAll(selectedItems);
        }
    
        // Phase 2: Save updated quantities
        itemRepository.saveAll(allSelectedItems.stream()
            .collect(Collectors.groupingBy(Item::getItemId, Collectors.collectingAndThen(Collectors.toList(), items -> {
                Item item = items.get(0);
                item.setItemAmount(item.getItemAmount()); // amount already updated
                return item;
            }))).values());
    
        return allSelectedItems;
    }

     // Add items to an existing box
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

     // Delete a box by ID
    public void deleteBoxById(Long id) {
        if (!boxRepository.existsById(id)) {
            throw new IllegalArgumentException("Box with ID " + id + " does not exist.");
        }
        boxRepository.deleteById(id);
    }

    // Retrieve a box by ID
    public Box getBoxById(Long id) {
        return boxRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Box with ID " + id + " not found."));
    }

    // Delete multiple items and return a summary
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

     // Handle item actions: update stock or delet
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
