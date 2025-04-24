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
            throw new IllegalStateException("Error: Not enough items in stock for box: " + boxName);
        }

        Collections.shuffle(availableItems);
        List<Item> selectedItems = availableItems.stream()
                .limit(numberOfItems)
                .collect(Collectors.toList());

        for (Item item : selectedItems) {
            item.setItemAmount(item.getItemAmount() - 1);
            itemRepository.save(item);
        }

        return selectedItems;
    }

    public List<Item> purchaseBoxesByBoxName(String boxName, int quantity) {
        List<Item> result = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            result.addAll(getRandomItemsFromBoxName(boxName, 1));
        }
        return result;
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
