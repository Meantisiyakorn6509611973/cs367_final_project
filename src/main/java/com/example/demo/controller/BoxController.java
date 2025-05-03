// This class defines REST endpoints for interacting with snack boxes and their items.

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
// Base path for all endpoints in this controller
public class BoxController {

    private final BoxService boxService;

    // Constructor to inject BoxService dependency
    public BoxController(BoxService boxService) {
        this.boxService = boxService;
    }

    @GetMapping("/ping")
    // Simple GET endpoint for server health check
    public String pingTest() {
        return "✅ Server is up and reachable!";
    }
    

    @GetMapping("/boxes")
    // Returns a list of all available boxes
    public List<Box> getAllBoxes() {
        return boxService.getAllBoxes();
    }

    @GetMapping("/boxes/boxname/{boxName}")
    // Returns boxes that match a specific name
    public List<Box> getBoxByBoxName(@PathVariable String boxName) {
        return boxService.getBoxesByBoxName(boxName);
    }

    @PostMapping("/boxes")
    // Creates a new box if its name is unique
    public ResponseEntity<?> createBox(@RequestBody Box box) {
        try {
            Box savedBox = boxService.saveBox(box);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBox);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/boxes/purchase/{boxName}")
    // Purchases a specific number of boxes by name; verifies stock before purchase
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
    // Purchases multiple boxes based on a list of box requests
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
     // Deletes a box by ID and returns remaining boxes
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
     // Updates or deletes item quantities based on request actions (e.g., increase, decrease, or remove)
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
     // Returns all items across all boxes
    public List<Item> getAllItems() {
        return boxService.getAllItems();
    }
}
