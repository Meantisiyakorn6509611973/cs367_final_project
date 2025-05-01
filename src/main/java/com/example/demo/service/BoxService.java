// Service layer for handling box operations including creation, purchase, and item stock management.
package com.example.demo.service;

import com.example.demo.model.Box;
import com.example.demo.model.History;
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
    private final HistoryService historyService;

    public BoxService(BoxRepository boxRepository, ItemRepository itemRepository, HistoryService historyService) {
        this.boxRepository = boxRepository;
        this.itemRepository = itemRepository;
        this.historyService = historyService;
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

    //  // Select random items from a box and reduce their quantities (used in purchasing logic)
    // public List<Item> getRandomItemsFromBoxName(String boxName, int numberOfItems) {
    //     List<Box> boxes = boxRepository.findByBoxName(boxName);
    //     if (boxes.isEmpty()) {
    //         throw new IllegalStateException("Error: No box found with name: " + boxName);
    //     }
    
    //     Box box = boxes.get(0);
    //     List<Item> availableItems = box.getItems().stream()
    //             .filter(item -> item.getItemAmount() > 0)
    //             .collect(Collectors.toList());
    
    //     if (availableItems.size() < numberOfItems) {
    //         throw new IllegalStateException("Error: Not enough item types in stock for box: " + boxName);
    //     }
    
    //     // Shuffle and choose candidates
    //     List<Item> shuffled = new ArrayList<>(availableItems);
    //     Collections.shuffle(shuffled);
    //     List<Item> selectedItems = shuffled.subList(0, numberOfItems);
    
    //     // Verify all selected items have enough quantity
    //     for (Item item : selectedItems) {
    //         if (item.getItemAmount() <= 0) {
    //             throw new IllegalStateException("Error: Item " + item.getItemName() + " is out of stock.");
    //         }
    //     }
    
    //     // All good — now deduct amounts
    //     for (Item item : selectedItems) {
    //         item.setItemAmount(item.getItemAmount() - 1);
    //     }
    
<<<<<<< HEAD
    //     itemRepository.saveAll(selectedItems);
    //     return selectedItems;
    // }    
  
    // Purchase boxes by name, ensuring availability and updating stock
=======
        itemRepository.saveAll(selectedItems);
        return selectedItems;
    }    

    // Purchase boxes by name, ensuring total item quantity is sufficient and selecting items randomly
<<<<<<< Updated upstream
=======
>>>>>>> a8a9818c208c8536ee745a6e715a057726cc7d22
>>>>>>> Stashed changes
    public List<Item> purchaseBoxesByBoxName(String boxName, int quantity) {
        // Step 1: Validate quantity is a positive number
        if (quantity <= 0) {
            throw new IllegalStateException("Quantity must be greater than 0.");
        }

<<<<<<< Updated upstream
        // Step 2: Find the box by its name
=======
<<<<<<< HEAD
        // Step 2: Find the box by name
=======
        // Step 2: Find the box by its name
>>>>>>> a8a9818c208c8536ee745a6e715a057726cc7d22
>>>>>>> Stashed changes
        List<Box> boxes = boxRepository.findByBoxName(boxName);
        if (boxes.isEmpty()) {
            throw new IllegalStateException("Error: No box found with name: " + boxName);
        }

        Box box = boxes.get(0);

<<<<<<< Updated upstream
        // Step 3: Filter out items with zero stock
=======
<<<<<<< HEAD
        // Step 3: Get available items from the box
=======
        // Step 3: Filter out items with zero stock
>>>>>>> a8a9818c208c8536ee745a6e715a057726cc7d22
>>>>>>> Stashed changes
        List<Item> availableItems = box.getItems().stream()
                .filter(item -> item.getItemAmount() > 0)
                .collect(Collectors.toList());

<<<<<<< Updated upstream
        // Step 4: Calculate total quantity available across all items
        int totalAvailable = availableItems.stream()
                .mapToInt(Item::getItemAmount)
                .sum();

        // Step 5: Ensure total available quantity is sufficient
        if (totalAvailable < quantity) {
            throw new IllegalStateException("Error: Not enough total item quantity available for box: " + boxName);
        }

        // Step 6: Randomly select items to fulfill the purchase request
        List<Item> selectedItems = new ArrayList<>();
        Random random = new Random();

        // Randomly pick items, allowing duplicates if stock permits
        while (selectedItems.size() < quantity) {
            Item randomItem = availableItems.get(random.nextInt(availableItems.size()));
            if (randomItem.getItemAmount() > 0) {
                selectedItems.add(randomItem);
                // Deduct 1 from stock immediately (in-memory update)
                randomItem.setItemAmount(randomItem.getItemAmount() - 1);
            }
        }

        // Step 7: Persist updated item quantities to the database
        itemRepository.saveAll(availableItems);

        // Step 8: Return the randomly selected items as response
        return selectedItems;
    }
=======
<<<<<<< HEAD
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
=======
        // Step 4: Calculate total quantity available across all items
        int totalAvailable = availableItems.stream()
                .mapToInt(Item::getItemAmount)
                .sum();

        // Step 5: Ensure total available quantity is sufficient
        if (totalAvailable < quantity) {
            throw new IllegalStateException("Error: Not enough total item quantity available for box: " + boxName);
        }

        // Step 6: Randomly select items to fulfill the purchase request
        List<Item> selectedItems = new ArrayList<>();
        Random random = new Random();

        // Randomly pick items, allowing duplicates if stock permits
        while (selectedItems.size() < quantity) {
            Item randomItem = availableItems.get(random.nextInt(availableItems.size()));
            if (randomItem.getItemAmount() > 0) {
                selectedItems.add(randomItem);
                // Deduct 1 from stock immediately (in-memory update)
                randomItem.setItemAmount(randomItem.getItemAmount() - 1);
>>>>>>> a8a9818c208c8536ee745a6e715a057726cc7d22
            }
        }

<<<<<<< HEAD
        // Step 6: Deduct item amounts and record history
        for (Item item : selectedItems) {
            item.setItemAmount(item.getItemAmount() - 1);
            // Record each item purchase in history
            historyService.recordItemPurchase(item.getItemName(), box.getBoxName());
        }
=======
        // Step 7: Persist updated item quantities to the database
        itemRepository.saveAll(availableItems);
>>>>>>> Stashed changes

        // Step 8: Return the randomly selected items as response
        return selectedItems;
    }
>>>>>>> a8a9818c208c8536ee745a6e715a057726cc7d22

        // Step 7: Save updated items
        itemRepository.saveAll(selectedItems);

        return selectedItems;
    }

    // Purchase multiple boxes based on a list of box requests
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
    
<<<<<<< Updated upstream
=======
<<<<<<< HEAD
        // Second phase: Apply changes
        for (Item item : allSelectedItems) {
            item.setItemAmount(item.getItemAmount() - 1);
            // Record the purchase in history using item's and box's name
            Box box = item.getBox(); // assuming Item has getBox() method
            String boxName = (box != null) ? box.getBoxName() : "Unknown";
            historyService.recordItemPurchase(item.getItemName(), boxName);
        }        
        itemRepository.saveAll(allSelectedItems);
=======
>>>>>>> Stashed changes
        // Phase 2: Save updated quantities
        itemRepository.saveAll(allSelectedItems.stream()
            .collect(Collectors.groupingBy(Item::getItemId, Collectors.collectingAndThen(Collectors.toList(), items -> {
                Item item = items.get(0);
                item.setItemAmount(item.getItemAmount()); // amount already updated
                return item;
            }))).values());
<<<<<<< Updated upstream
=======
>>>>>>> a8a9818c208c8536ee745a6e715a057726cc7d22
>>>>>>> Stashed changes
    
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
