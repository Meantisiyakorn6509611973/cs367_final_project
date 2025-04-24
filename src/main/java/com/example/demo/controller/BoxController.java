package com.example.demo.controller;

import com.example.demo.model.Box;
import com.example.demo.model.Item;
import com.example.demo.service.BoxService;
import com.example.demo.dto.BoxPurchaseRequest;
import com.example.demo.dto.PurchaseResponse;
import com.example.demo.dto.ItemQuantityUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class BoxController {

    private final BoxService boxService;

    public BoxController(BoxService boxService) {
        this.boxService = boxService;
    }

    @GetMapping("/boxes")
    public List<Box> getAllBoxes() {
        return boxService.getAllBoxes();
    }

    @GetMapping("/boxes/boxname/{boxName}")
    public List<Box> getBoxByBoxName(@PathVariable String boxName) {
        return boxService.getBoxesByBoxName(boxName);
    }

    @PostMapping("/boxes")
    public Box createBox(@RequestBody Box box) {
        return boxService.saveBox(box);
    }

    @PostMapping("/boxes/purchase/{boxName}")
    public ResponseEntity<?> purchaseBoxes(
            @PathVariable String boxName,
            @RequestParam(defaultValue = "1") int quantity) {
        try {
            List<Item> items = boxService.purchaseBoxesByBoxName(boxName, quantity);
            String message = "You received: " +
                    items.stream().map(Item::getItemName).collect(Collectors.joining(", "));
            return ResponseEntity.ok(new PurchaseResponse(message, items));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/boxes/purchase")
    public ResponseEntity<?> purchaseMultipleBoxes(@RequestBody List<BoxPurchaseRequest> boxRequests) {
        try {
            List<Item> items = boxService.purchaseMultipleBoxes(boxRequests);
            String message = "You received: " +
                    items.stream().map(Item::getItemName).collect(Collectors.joining(", "));
            return ResponseEntity.ok(new PurchaseResponse(message, items));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/boxes/{id}")
    public ResponseEntity<?> deleteBox(@PathVariable Long id) {
        try {
            Box deletedBox = boxService.getBoxById(id);
            boxService.deleteBoxById(id);
            List<Box> remainingBoxes = boxService.getAllBoxes();

            return ResponseEntity.ok(Map.of(
                    "message", "Box deleted successfully.",
                    "deletedBox", deletedBox,
                    "remainingBoxes", remainingBoxes
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/items/quantity")
    public ResponseEntity<?> updateItemQuantities(@RequestBody List<ItemQuantityUpdateRequest> requests) {
        try {
            List<Map<String, Object>> results = new ArrayList<>();
            for (ItemQuantityUpdateRequest req : requests) {
                results.add(boxService.handleItemAction(req));
            }
    
            return ResponseEntity.ok(Map.of(
                "message", "Item actions completed successfully.",
                "results", results,
                "remainingItems", boxService.getAllItems()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }    
    
    @GetMapping("/items")
    public List<Item> getAllItems() {
        return boxService.getAllItems();
    }
}
